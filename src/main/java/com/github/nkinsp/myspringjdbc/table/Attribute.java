package com.github.nkinsp.myspringjdbc.table;

import java.beans.PropertyDescriptor;
import java.util.Map;

/**
 * 属性映射
 * @author hanjiang.Yue
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Attribute {
	
	
	
	private PropertyDescriptor property;
	
	/**
	 * 实体字段
	 */
	private String fieldName;
	
	/**
	 * 表字段
	 */
	private String columnName;
	
	/**
	 * 属性类型
	 */
	private AttributeType type = AttributeType.ENTITY_FIELD;
	
	private Class<?> classType;
	
	/**
	 *  
	 * @author hanjiang.Yue
	 * @param target
	 * @return
	 */
	public <T> T getValue(Object target) {
		if(type ==  AttributeType.MAP_KEY) {
			Map map = (Map) target;
			return (T) map.get(fieldName);
		}
		try {
			return (T) property.getReadMethod().invoke(target);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public void setValue(Object target,Object value) {
		if(type == AttributeType.MAP_KEY) {
			Map map = (Map) target;
			map.put(fieldName, value);
			return;
		}
		try {
			property.getWriteMethod().invoke(target,value);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	

	public PropertyDescriptor getProperty() {
		return property;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getColumnName() {
		return columnName;
	}

	public Attribute(PropertyDescriptor property, String fieldName, String columnName) {
		super();
		this.property = property;
		this.fieldName = fieldName;
		this.columnName = columnName;
		this.classType = property.getPropertyType();
		
	}
	
	

	public Attribute(String fieldName, String columnName, AttributeType type, Class<?> classType) {
		super();
		this.fieldName = fieldName;
		this.columnName = columnName;
		this.type = type;
		this.classType = classType;
	}

	public Class<?> getClassType() {
		return classType;
	}

	public void setClassType(Class<?> classType) {
		this.classType = classType;
	}
	

	public AttributeType getType() {
		return type;
	}

	public void setType(AttributeType type) {
		this.type = type;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Attribute() {
		super();
	}
	
	
}
