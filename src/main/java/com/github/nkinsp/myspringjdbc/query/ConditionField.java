package com.github.nkinsp.myspringjdbc.query;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;




public class ConditionField {

	
	private final Map<String, Object> annotationMap = new HashMap<String, Object>();
	
	private String fieldName;
	
	private ConditionQuery target;
	
	private Object value;
	
	@SuppressWarnings("unchecked")
	public <T> T getAnnotatioValue(String name) {
		
		return (T) annotationMap.get(name);
	}
	
	



	public ConditionField(Field field, ConditionQuery target) {
		super();
	
		this.target = target;
		this.value = getFieldValue(field);
		this.fieldName = field.getName();
		this.initAnnotationMap(field);
	
	}
	
	
	private void initAnnotationMap(Field field) {
		Annotation[] annotations = field.getAnnotations();
		for (Annotation annotation : annotations) {
			Class<? extends Annotation> annotationType = annotation.annotationType();
			
			Method[] methods = annotationType.getDeclaredMethods();
			for (Method method : methods) {
				
				try {
					Object value = method.invoke(annotation);
					this.annotationMap.put(method.getName(), value);
				} catch (Exception e) {
					
					throw new RuntimeException(e);
				} 
				
			}
			
		}
	}
	
	private Object getFieldValue(Field field) {
		
		try {
			 PropertyDescriptor pd = new PropertyDescriptor(field.getName(), target.getClass());
			 return pd.getReadMethod().invoke(target);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	public String getFieldName() {
		return fieldName;
	}







	public Object getValue() {
		return value;
	}
	
	
	
}
