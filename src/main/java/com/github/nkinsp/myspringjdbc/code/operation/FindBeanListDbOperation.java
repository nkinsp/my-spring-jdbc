package com.github.nkinsp.myspringjdbc.code.operation;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import com.github.nkinsp.myspringjdbc.activerecord.MapModel;
import com.github.nkinsp.myspringjdbc.annotation.CascadeEntity;
import com.github.nkinsp.myspringjdbc.annotation.SelectColumnMapper;
import com.github.nkinsp.myspringjdbc.annotation.SelectEntityMapping;
import com.github.nkinsp.myspringjdbc.annotation.Table;
import com.github.nkinsp.myspringjdbc.query.Query;
import com.github.nkinsp.myspringjdbc.query.SelectColumnMapperRender;
import com.github.nkinsp.myspringjdbc.query.select.DefaultSelectColumnMapperRender;
import com.github.nkinsp.myspringjdbc.table.Attribute;
import com.github.nkinsp.myspringjdbc.table.TableMapping;
import com.github.nkinsp.myspringjdbc.util.ClassUtils;
import com.github.nkinsp.myspringjdbc.util.StringUtils;



public class FindBeanListDbOperation<T,En> extends AbstractDbOperation<T>{

	private Class<En> enClass;
	
	public FindBeanListDbOperation(Query<T> query,Class<En> enClass) {
		super(query);
		this.enClass = enClass;
	}

	
	
	
	

	
	
	protected String builderQuerySql() {
		String sql = query.getSqlBuilder().buildSelectSQL();
		return sql;
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<En> getModelList(String sql,Object[] params){

		List<Map<String, Object>> mapList = context.queryForList(sql, query.getParams().toArray());
		List<En> modelList = mapList.stream().map(map -> {
			MapModel mapModel = (MapModel) BeanUtils.instantiate(enClass);
			mapModel.putAll(map);
			return (En) mapModel;
		}).collect(Collectors.toList());
		return modelList;
	}
	
   public List<En> findList(){
	   
	   String sql = builderQuerySql();
	   
		if(log.isInfoEnabled() && context.isShowSqlInfo()) {
			log.info("==> execute [sql={},params={}]",sql,query.getParams());
		}
		if(MapModel.class.isAssignableFrom(enClass)) {
			return getModelList(sql, query.getParams().toArray());
		}
		return context.query(sql,new BeanRowMapper<En>(enClass, tableMapping), query.getParams().toArray());
   }
	
	
	@Override
	public Object dbAdapter() {
		
		
		/**
		 * 是否有查询字段映射
		 */
		if(ClassUtils.hasAnnotation(enClass, SelectEntityMapping.class)) {
			
			Collection<Field> values = ClassUtils.getClassFieldMap(enClass).values();
			for (Field field : values) {
				SelectColumnMapper mapper = field.getAnnotation(SelectColumnMapper.class);
				if(mapper != null) {
					String column = StringUtils.humpToLine(field.getName());
					String value = mapper.value();
					if(!StringUtils.isEmpty(value)) {
						query.select(value+" as "+column);
					}else {
						query.select(column);
					}
					
					Class<? extends SelectColumnMapperRender> render = mapper.render();
					
					if(!render.equals(DefaultSelectColumnMapperRender.class)) {
						ClassUtils.newInstance(render).render(query);
					}
					continue;
				}
			}
			
		}
		
		 List<En> list = findList();
	
		
		 if(!ClassUtils.hasAnnotation(enClass, CascadeEntity.class)) {
			 return list;
		 }
		 return context.executeCascadeEntityAdapter(list, tableMapping,enClass);
		
	}



	public static class BeanRowMapper<T> extends BeanPropertyRowMapper<T>{

		private static Log log = LogFactory.getLog(BeanRowMapper.class);
		
		private TableMapping<?> tableMapping;
		
		private Class<T> entityClass;

		public BeanRowMapper(Class<T> entityClass,TableMapping<?> tableMapping) {
			this.entityClass = entityClass;
			Table table = entityClass.getAnnotation(Table.class);
			if(table == null) {
				super.setMappedClass(entityClass);
			}else {
				this.tableMapping = tableMapping;
			}
			
		
		}


		@Override
		public T mapRow(ResultSet rs, int rowNum) throws SQLException {

			if(tableMapping == null) {
				return super.mapRow(rs, rowNum);
			}
			return getResultObject(rs, entityClass);
		}



		/**
		 * 构建对象
		 * @param rs
		 * @param mappedClass
		 * @return
		 * @throws SQLException
		 */
		private T getResultObject(ResultSet rs, Class<T> mappedClass) throws SQLException {
			T mappedObject = BeanUtils.instantiateClass(mappedClass);
			BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
			ResultSetMetaData metaData = rs.getMetaData();
			int count = metaData.getColumnCount();
			for (int index = 1; index <= count; index++) {
				String columnName = JdbcUtils.lookupColumnName(metaData, index);
				Attribute tableField = tableMapping.getPropertyByColumn(columnName);
				if(tableField == null) {
					continue;
				}
				PropertyDescriptor pd = tableField.getProperty();
				try {
					Object value = JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
					bw.setPropertyValue(pd.getName(), value);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					log.error("Mapping column '" + columnName + "' to property '" + pd.getName() +
							"' of type '" +  org.springframework.util.ClassUtils.getQualifiedName(pd.getPropertyType()) + " fail'");
				}
			}
			return mappedObject;
		}

	}

}
