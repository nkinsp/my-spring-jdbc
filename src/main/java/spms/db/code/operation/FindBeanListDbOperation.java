package spms.db.code.operation;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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
import org.springframework.util.ClassUtils;

import spms.db.activerecord.MapModel;
import spms.db.annotation.Table;
import spms.db.query.Query;
import spms.db.table.Attribute;
import spms.db.table.TableMapping;



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
		
		return findList();
		
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
							"' of type '" + ClassUtils.getQualifiedName(pd.getPropertyType()) + " fail'");
				}
			}
			return mappedObject;
		}

	}

}
