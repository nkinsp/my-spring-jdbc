package com.github.nkinsp.myspringjdbc.query.impl;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.github.nkinsp.myspringjdbc.annotation.OneToMany;
import com.github.nkinsp.myspringjdbc.code.DbContext;
import com.github.nkinsp.myspringjdbc.code.repository.QueryRepository;
import com.github.nkinsp.myspringjdbc.query.CascadeEntityAdapter;
import com.github.nkinsp.myspringjdbc.query.CascadeValueConvert;
import com.github.nkinsp.myspringjdbc.table.TableMapping;
import com.github.nkinsp.myspringjdbc.util.ClassUtils;
import com.github.nkinsp.myspringjdbc.util.ObjectUtils;

public class OneToManyCascadeEntityAdapter implements CascadeEntityAdapter<OneToMany>{

	@Override
	public boolean support(Class<? extends Annotation> annotationClass) {
		// TODO Auto-generated method stub
		return annotationClass.equals(OneToMany.class);
	}

	@Override
	public <T> void adapter(List<T> data, TableMapping<?> tableMapping,Class<T> enClass, Field field, Annotation annotation, DbContext dbContext) {
	
		
		if(CollectionUtils.isEmpty(data)) {
			return;
		}
		
		OneToMany manyToMany = (OneToMany) annotation;
		
		String fieldName = tableMapping.getIdProperty().getFieldName();

		List<Object> fieldValues = ObjectUtils.getFieldValues(data, fieldName).stream().collect(Collectors.toList());
		
		if(CollectionUtils.isEmpty(fieldValues)) {
			return;
		}
		
		
		QueryRepository<?,Object> repository =dbContext.table(manyToMany.joinTableClass());	
		CascadeValueConvert convert = ClassUtils.newInstance(manyToMany.convert());
		
		
		ParameterizedType type =(ParameterizedType) field.getGenericType();
		@SuppressWarnings("unchecked")
		Class<Object> convertType = (Class<Object>) type.getActualTypeArguments()[0];
		
	
		
		String joinTableField = manyToMany.joinTableField();
		
		
		 Map<Object, List<Object>> dataMap = repository.findList( find->find.where().in(joinTableField, fieldValues.toArray()))
				.stream()
				.collect(Collectors.groupingBy(x->ObjectUtils.getFieldValue(x, joinTableField)));
		
	
		
	    
		PropertyDescriptor pd = ClassUtils.findPropertyDescriptor(field.getName(), enClass);
		
	
		
		data.forEach(x->{
			
		
			Object value = ObjectUtils.getFieldValue(x, fieldName);
			
			try {
				
				List<?> values = dataMap.get(value).stream().map(v->convert.convert(v,convertType)).collect(Collectors.toList());
				pd.getWriteMethod().invoke(x, values);
			
			} catch (Exception e) {
				throw new RuntimeException(e);
			} 
			
		});
		
		
	}

}
