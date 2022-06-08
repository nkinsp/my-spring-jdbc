package com.github.nkinsp.myspringjdbc.code;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.github.nkinsp.myspringjdbc.activerecord.Model;
import com.github.nkinsp.myspringjdbc.cache.CacheManager;
import com.github.nkinsp.myspringjdbc.code.repository.QueryRepository;
import com.github.nkinsp.myspringjdbc.pk.PrimaryKeyGenerated;
import com.github.nkinsp.myspringjdbc.pk.gen.DefaultPrimaryKeyGenerated;
import com.github.nkinsp.myspringjdbc.query.CascadeEntityAdapter;
import com.github.nkinsp.myspringjdbc.query.ConditionAdapter;
import com.github.nkinsp.myspringjdbc.query.ConditionField;
import com.github.nkinsp.myspringjdbc.query.ConditionQuery;
import com.github.nkinsp.myspringjdbc.query.Query;
import com.github.nkinsp.myspringjdbc.query.conditions.EqConditionAdapter;
import com.github.nkinsp.myspringjdbc.query.conditions.GeConditionAdapter;
import com.github.nkinsp.myspringjdbc.query.conditions.GtConditionAdapter;
import com.github.nkinsp.myspringjdbc.query.conditions.InConditionAdapter;
import com.github.nkinsp.myspringjdbc.query.conditions.LeConditionAdapter;
import com.github.nkinsp.myspringjdbc.query.conditions.LikeConditionAdapter;
import com.github.nkinsp.myspringjdbc.query.conditions.LtConditionAdapter;
import com.github.nkinsp.myspringjdbc.query.conditions.NeConditionAdapter;
import com.github.nkinsp.myspringjdbc.query.conditions.QueryConditionAdapter;
import com.github.nkinsp.myspringjdbc.query.impl.DB2DialectQueryImpl;
import com.github.nkinsp.myspringjdbc.query.impl.H2DialectQueryImpl;
import com.github.nkinsp.myspringjdbc.query.impl.ManyToManyCascadeEntityAdapter;
import com.github.nkinsp.myspringjdbc.query.impl.OneToOneCascadeEntityAdapter;
import com.github.nkinsp.myspringjdbc.query.impl.MySqlDialectQueryImpl;
import com.github.nkinsp.myspringjdbc.query.impl.OneToManyCascadeEntityAdapter;
import com.github.nkinsp.myspringjdbc.query.impl.OracleDialectQueryImpl;
import com.github.nkinsp.myspringjdbc.query.impl.PostgreDialectQueryImpl;
import com.github.nkinsp.myspringjdbc.query.impl.SQLServerDialectQueryImpl;
import com.github.nkinsp.myspringjdbc.query.impl.SQLiteDialectQueryImpl;
import com.github.nkinsp.myspringjdbc.table.TableMapping;
import com.github.nkinsp.myspringjdbc.util.ClassUtils;
import com.github.nkinsp.myspringjdbc.util.ObjectUtils;

public class DbContext extends JdbcTemplate {

	
	private Class<?> dialectClass;
	
	private final Map<String, TableMapping<?>> tableMapping = new ConcurrentHashMap<>();

	private PrimaryKeyGenerated primaryKeyGenerated = new DefaultPrimaryKeyGenerated();
		
	private boolean showSqlInfo = true;
	
	private CacheManager cacheManager;
	
	
	private final List<ConditionAdapter<?>> conditionAdapters = new ArrayList<ConditionAdapter<?>>();
	
	private final List<CascadeEntityAdapter<?>> cascadeEntityAdapters = new ArrayList<CascadeEntityAdapter<?>>();
	
	
	@SuppressWarnings("unchecked")
	public synchronized <T> TableMapping<T> findTableMapping(Class<T> tableClass) {
		String name = tableClass.getName();
		TableMapping<?> mapping = tableMapping.get(name);
		if(mapping == null) {
			mapping = new TableMapping<>(tableClass);
			tableMapping.put(name, mapping);
		}
		return (TableMapping<T>) mapping;
	}

	
	@SuppressWarnings("unchecked")
	public <M> Query<M> createQuery(Class<M> tableClass) {
		try {
			return (Query<M>) this.dialectClass.getConstructor(TableMapping.class, DbContext.class).newInstance(findTableMapping(tableClass), this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public  <T,Id> QueryRepository<T, Id> table(Class<T> tableClass){
		return new QueryRepository<T, Id>() {
			@Override
			public Query<T> createQuery() {
				return DbContext.this.createQuery(tableClass);
			}
			

			@Override
			public DbContext getDbContext() {
				// TODO Auto-generated method stub
				return DbContext.this;
			}
			
		};
	}
	

	public boolean isShowSqlInfo() {
		return showSqlInfo;
	}


	public void setShowSqlInfo(boolean showSqlInfo) {
		this.showSqlInfo = showSqlInfo;
	}


	public PrimaryKeyGenerated getPrimaryKeyGenerated() {
		return primaryKeyGenerated;
	}


	public void setPrimaryKeyGenerated(PrimaryKeyGenerated primaryKeyGenerated) {
		this.primaryKeyGenerated = primaryKeyGenerated;
	}


	public DbContext(DataSource dataSource) {
		this(dataSource, null);
	}
	
	public DbContext(DataSource dataSource,CacheManager cacheManager) {
	
		super(dataSource);
		this.cacheManager = cacheManager;
		this.dialectClass = getDialectClass();
		this.initConditionAdapter();
		this.intCascadeEntityAdapters();
		
	}

	public Class<?> getDialectClass() {
		Connection connection = null;
		try {
			connection = getDataSource().getConnection();
			DatabaseMetaData metaData =connection.getMetaData();
			String url = metaData.getURL().toLowerCase();
			Map<String, Class<?>> dbTypeMap = new HashMap<>();
			dbTypeMap.put("jdbc:mysql:",MySqlDialectQueryImpl.class );
			dbTypeMap.put("jdbc:sqlite:", SQLiteDialectQueryImpl.class);
			dbTypeMap.put("jdbc:oracle:",OracleDialectQueryImpl.class);
			dbTypeMap.put("jdbc:postgresql:",PostgreDialectQueryImpl.class);
			dbTypeMap.put("jdbc:db2:",DB2DialectQueryImpl.class);
			dbTypeMap.put("jdbc:sqlserver:",SQLServerDialectQueryImpl.class);
			dbTypeMap.put("jdbc:h2:", H2DialectQueryImpl.class);
			for (Entry<String, Class<?>> en : dbTypeMap.entrySet()) {
				if(url.startsWith(en.getKey())) {
					return en.getValue();
				}
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}


	
	
	
	public void activeRecordEnable() {
	
		Model.init(this);
	}
	
	
	private void initConditionAdapter() {
		
		
		
		
		ConditionAdapter<?>[] apapters = { 
				new EqConditionAdapter(),
				new NeConditionAdapter(), 
				new LtConditionAdapter(),
				new LeConditionAdapter(), 
				new GtConditionAdapter(),
				new GeConditionAdapter(),
				new InConditionAdapter(),
			    new LikeConditionAdapter(),
				new QueryConditionAdapter() 
		};
		
		for (ConditionAdapter<?> adapter : apapters) {
			this.addConditionAdapter(adapter);
		}
		
	}
	
	
	private void intCascadeEntityAdapters() {

		this.cascadeEntityAdapters.addAll(Arrays.asList(

				new OneToOneCascadeEntityAdapter(),
				new ManyToManyCascadeEntityAdapter(),
				new OneToManyCascadeEntityAdapter()

		));

	}
	
	
	public void addConditionAdapter(ConditionAdapter<?> adapter) {
		
		this.conditionAdapters.add(adapter);
	}
	

	public <T> List<T> executeCascadeEntityAdapter(List<T> data,TableMapping<?> tableMapping,Class<T> enClass ){
			
	   Collection<Field> fileds = ClassUtils.getClassFieldMap(enClass).values();
	   
		for (Field field : fileds) {
			
			Annotation[] annotations = field.getAnnotations();
			for (Annotation an : annotations) {
				
				for (CascadeEntityAdapter<?> adapter: this.cascadeEntityAdapters) {
					if(adapter.support(an.annotationType())) {
						  adapter.adapter(data,tableMapping,enClass,field,an,this);
					}
					
				}
			}
			
		}

		
		return data;
		
	}
	
	public <T> void executeConditionAdapter(ConditionQuery conditionQuery, Query<T> query) {

		Map<String, Field> fieldMap = ClassUtils.getClassFieldMap(conditionQuery.getClass());
		Collection<Field> fileds = fieldMap.values();
		int i = 0;
		ConditionField conditionField = null;
		for (Field field : fileds) {
			conditionField = new ConditionField(field, conditionQuery);
			if (!ObjectUtils.isEmpty(conditionField.getValue())) {
				Annotation[] annotations = field.getAnnotations();
				for (Annotation annotation : annotations) {
					for (ConditionAdapter<?> adapter : this.conditionAdapters) {
						if (adapter.match(annotation.annotationType())) {
							Consumer<Query<T>> consumer = adapter.adapter(conditionField);
							if (i == 0) {
								query.notEmptyThen(conditionField.getValue(), consumer);
							} else {
								query.andNotEmptyThen(conditionField.getValue(), consumer);
							}
							i++;

						}
					}

				}
			}

		}

	}


	public CacheManager getCacheManager() {
		return cacheManager;
	}


	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
}
