package spms.db.query.conditions;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.function.Consumer;


import spms.db.query.ConditionAdapter;
import spms.db.query.ConditionField;
import spms.db.query.Query;
import spms.db.query.QueryConvert;
import spms.db.util.ClassUtils;
import spms.db.util.StringUtils;

public abstract class AbstractConditionAdapter<A extends Annotation> implements ConditionAdapter<A>{


	
	@SuppressWarnings("unchecked")
	private Class<? extends Annotation> getTypeClass(){
		return (Class<? extends Annotation>) (((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}
	
	
	@Override
	public boolean match(Class<? extends Annotation> annotationClass) {
		return annotationClass.equals(getTypeClass());
	}
	
	
	
	public Object convert(Class<? extends QueryConvert<Object,Object>> converClass,Object value) {
		
		QueryConvert<Object,Object> convert = ClassUtils.newInstance(converClass);

		return convert.convert(value);
	}
	
	abstract <T> Consumer<Query<T>> adapter(String name,Object value);
	
	
	@Override
	public <T> Consumer<Query<T>> adapter(ConditionField field) {
		String annotationValue = field.getAnnotatioValue("value");
		Class<? extends QueryConvert<Object,Object>> convertClass = field.getAnnotatioValue("convert");
		Object value = convertClass != null?convert(convertClass, field.getValue()):field.getValue();		
		String name = StringUtils.isEmpty(annotationValue)?StringUtils.humpToLine(field.getFieldName()):annotationValue;	
		return adapter(name, value);
	}
	


}
