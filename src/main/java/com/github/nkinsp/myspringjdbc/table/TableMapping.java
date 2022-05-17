package com.github.nkinsp.myspringjdbc.table;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.nkinsp.myspringjdbc.activerecord.ConfigColumn;
import com.github.nkinsp.myspringjdbc.activerecord.MapModel;
import com.github.nkinsp.myspringjdbc.annotation.Column;
import com.github.nkinsp.myspringjdbc.annotation.KeySequence;
import com.github.nkinsp.myspringjdbc.annotation.Table;
import com.github.nkinsp.myspringjdbc.exception.NotFoundTableException;
import com.github.nkinsp.myspringjdbc.util.ClassUtils;
import com.github.nkinsp.myspringjdbc.util.StringUtils;


/**
 * 表映射
 * @author hanjiang.Yue
 * @param <T>
 */
public class TableMapping<T> {


	private Class<T> tableClass;
	
	private String tableName;
	
	private IdType idType;
	
	private String primaryKeyName;
	
	private final Map<String, Attribute> fieldColumMap;
	
	private final Map<String, Attribute> columFieldMap;

	private final List<String> columns;
	
	private boolean cache ;
	
	private long cacheTime;
	
	private Attribute primaryKey;
	
	private String keySequence;
	
	private boolean logicDelete;
	
	private String logicDeleteName;
	
	
	
	/**
	 * @param tableClass
	 */
	public TableMapping(Class<T> tableClass) {
		this.tableClass = tableClass;
		this.columns = new ArrayList<>();
		this.fieldColumMap = new LinkedHashMap<>();
		this.columFieldMap = new LinkedHashMap<>();
		//加载信息
		this.initialize();
	}
	
	
	/**
	 * 初始化加载...
	 * 
	 * @author hanjiang.Yue
	 */
	private void initialize() {
		Table table = tableClass.getAnnotation(Table.class);
		// No Table Annotation
		if (table == null) {
			throw new NotFoundTableException("Not Found @Table Class" + this.tableClass);
		}
		KeySequence sequence = tableClass.getAnnotation(KeySequence.class);
		
		this.tableName = StringUtils.isEmpty(table.name())?StringUtils.humpToLine(tableClass.getSimpleName()):table.name();
		this.primaryKeyName = table.id();
		this.idType = table.idType();
		this.cache = table.cache();
		this.cacheTime = table.cacheTime();
		this.logicDelete = table.logicDeleteEnable();
		this.logicDeleteName = table.logicDeleteName();
		this.keySequence = sequence != null?sequence.value():null;
		
		// 实体是一个Map
		if (MapModel.class.isAssignableFrom(tableClass)) {
			
			MapModel<?, ?> model = (MapModel<?, ?>) ClassUtils.newInstance(tableClass);
			// 加载字段
			model.confingColumns(new ConfigColumn() {
				
				@Override
				public ConfigColumn add(String name, Class<?> classType) {
					addColumn(new Attribute(name, name, AttributeType.MAP_KEY, classType));
					return this;
				}
			});

			Attribute idAttr = getPropertyByColumn(getPrimaryKey());
			if (idAttr == null) {
				idAttr = new Attribute(getPrimaryKey(), getPrimaryKey(), AttributeType.MAP_KEY, model.idClass);
			}
			// 主键信息
			setIdProperty(idAttr);
			return;
		}

		
		Collection<PropertyDescriptor> descriptors = ClassUtils.getPropertyDescriptors(tableClass);
		
		for (PropertyDescriptor pd : descriptors) {
			// 获取字段信息
			String fieldName = pd.getName();
			Field field = getField(fieldName);
			Column column = (field == null) ? null : field.getDeclaredAnnotation(Column.class);
			// 表字段
			String colName = (column == null) ? StringUtils.humpToLine(fieldName) : column.value();
			// Property 对象
			addColumn(new Attribute(pd, fieldName, colName));
		}
	
		// 主键信息
		setIdProperty(getPropertyByColumn(primaryKeyName));

	}

	public TableMapping<T> addColumn(Attribute attr){
		this.columns.add(attr.getColumnName());
		this.columFieldMap.put(attr.getColumnName(), attr);
		this.fieldColumMap.put(attr.getFieldName(), attr);
		return this;
	}
	
	public TableMapping<T> addColumn(String name,Class<?> typeClass,AttributeType type){
		Attribute property  = new Attribute();
		property.setClassType(typeClass);
		property.setColumnName(name);
		property.setFieldName(name);
		property.setType(type);
		return addColumn(property);
	}
	
	/**
	 * 获取字段信息 
	 * @author hanjiang.Yue
	 * @param name
	 * @return
	 */
	private Field getField(String name) {
		return ClassUtils.findField(name, tableClass);
	}
	
	/**
	 * 通过列名获取
	 * @author hanjiang.Yue
	 * @param columnName
	 * @return
	 */
	public Attribute getPropertyByColumn(String  columnName) {
		return this.columFieldMap.get(columnName);
	}
	
	/**
	 * 通过字段名获取
	 * @author hanjiang.Yue
	 * @param fieldName
	 * @return
	 */
	public Attribute getPropertyByField(String fieldName) {
		return this.fieldColumMap.get(fieldName);
	}
	
	public String getColumnName(String fieldName) {
		Attribute property = getPropertyByField(fieldName);
		if(property != null) {
			return property.getColumnName();
		}
		return fieldName;
	}
	
	
	public Class<T> getTableClass() {
		return tableClass;
	}


	public String getTableName() {
		return tableName;
	}


	public IdType getIdType() {
		return idType;
	}


	public String getPrimaryKey() {
		return primaryKeyName;
	}


	public List<String> getColumns() {
		return columns;
	}

	
	public Collection<Attribute> getPropertys(){
		return this.columFieldMap.values();
	}


	public boolean isCache() {
		return cache;
	}


	public void setCache(boolean cache) {
		this.cache = cache;
	}


	public long getCacheTime() {
		return cacheTime;
	}


	public void setCacheTime(int cacheTime) {
		this.cacheTime = cacheTime;
	}
	
	public Class<?> getIdClassType(){
		return primaryKey.getClassType();
	}


	public Attribute getIdProperty() {
		return primaryKey;
	}
	
	public Object getIdValue(Object target) {
		return getIdProperty().getValue(target);
	}


	public void setIdProperty(Attribute idProperty) {
		this.primaryKey = idProperty;
	}


	public String getKeySequence() {
		return keySequence;
	}


	public void setKeySequence(String keySequence) {
		this.keySequence = keySequence;
	}


	public boolean isLogicDelete() {
		return logicDelete;
	}


	public void setLogicDelete(boolean logicDelete) {
		this.logicDelete = logicDelete;
	}


	public String getLogicDeleteName() {
		return logicDeleteName;
	}


	public void setLogicDeleteName(String logicDeleteName) {
		this.logicDeleteName = logicDeleteName;
	}
}
