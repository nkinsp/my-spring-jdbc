package spms.db.code;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import spms.db.activerecord.Model;
import spms.db.pk.PrimaryKeyGenerated;
import spms.db.pk.gen.DefaultPrimaryKeyGenerated;
import spms.db.query.ConditionAdapter;
import spms.db.query.ConditionField;
import spms.db.query.ConditionQuery;
import spms.db.query.Query;
import spms.db.query.conditions.EqConditionAdapter;
import spms.db.query.conditions.GeConditionAdapter;
import spms.db.query.conditions.GtConditionAdapter;
import spms.db.query.conditions.InConditionAdapter;
import spms.db.query.conditions.LeConditionAdapter;
import spms.db.query.conditions.LtConditionAdapter;
import spms.db.query.conditions.NeConditionAdapter;
import spms.db.query.conditions.QueryConditionAdapter;
import spms.db.query.impl.DB2DialectQueryImpl;
import spms.db.query.impl.H2DialectQueryImpl;
import spms.db.query.impl.MySqlDialectQueryImpl;
import spms.db.query.impl.OracleDialectQueryImpl;
import spms.db.query.impl.PostgreDialectQueryImpl;
import spms.db.query.impl.SQLServerDialectQueryImpl;
import spms.db.query.impl.SQLiteDialectQueryImpl;
import spms.db.table.TableMapping;
import spms.db.util.ClassUtils;
import spms.db.util.ObjectUtils;

public class DbContext extends JdbcTemplate {

	
	private Class<?> dialectClass;
	
	private final Map<String, TableMapping<?>> tableMapping = new ConcurrentHashMap<>();
	
	
	private PrimaryKeyGenerated primaryKeyGenerated = new DefaultPrimaryKeyGenerated();
		
	private boolean showSqlInfo = true;
	
	private final List<ConditionAdapter<?>> conditionAdapters = new ArrayList<ConditionAdapter<?>>();
	
	
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
	
		super(dataSource);
		this.dialectClass = getDialectClass();
		this.initConditionAdapter();
		
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
				new QueryConditionAdapter() 
		};
		
		for (ConditionAdapter<?> adapter : apapters) {
			this.addConditionAdapter(adapter);
		}
		
	}
	
	
	public void addConditionAdapter(ConditionAdapter<?> adapter) {
		
		this.conditionAdapters.add(adapter);
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
	
}
