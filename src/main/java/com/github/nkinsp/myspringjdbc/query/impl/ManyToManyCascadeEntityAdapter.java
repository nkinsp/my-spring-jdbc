package com.github.nkinsp.myspringjdbc.query.impl;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.github.nkinsp.myspringjdbc.annotation.ManyToMany;
import com.github.nkinsp.myspringjdbc.code.DbContext;
import com.github.nkinsp.myspringjdbc.code.repository.QueryRepository;
import com.github.nkinsp.myspringjdbc.query.CascadeEntityAdapter;
import com.github.nkinsp.myspringjdbc.query.CascadeValueConvert;
import com.github.nkinsp.myspringjdbc.util.ClassUtils;
import com.github.nkinsp.myspringjdbc.util.ObjectUtils;

public class ManyToManyCascadeEntityAdapter implements CascadeEntityAdapter<ManyToMany>{

	
	@Override
	public boolean support(Class<? extends Annotation> annotationClass) {
		// TODO Auto-generated method stub
		return annotationClass.equals(ManyToMany.class);
	}

	@Override
	public <T> void adapter(List<T> data, Class<T> enClass, Field field,Annotation annotation, DbContext dbContext) {
		
		if(CollectionUtils.isEmpty(data)) {
			return;
		}
		
		ManyToMany manyToMany = (ManyToMany) annotation;

		List<Object> fieldValues = ObjectUtils.getFieldValues(data, manyToMany.joinField()).stream().collect(Collectors.toList());
		
		if(CollectionUtils.isEmpty(fieldValues)) {
			return;
		}
		
		
		QueryRepository<?,Object> repository =dbContext.table(manyToMany.joinTableClass());	
		CascadeValueConvert convert = ClassUtils.newInstance(manyToMany.convert());
		
		
		ParameterizedType type =(ParameterizedType) field.getGenericType();
		Class<?> convertType = (Class<?>) type.getActualTypeArguments()[0];
		
		
		Map<Object, List<T>> dataMap = repository.findList(enClass, find->find.where().in(manyToMany.joinTableField(), fieldValues.toArray()))
				.stream()
				.collect(Collectors.groupingBy(x->ObjectUtils.getFieldValue(x, manyToMany.joinTableField())));
		
	
		
	    
		PropertyDescriptor pd = ClassUtils.findPropertyDescriptor(field.getName(), enClass);
		
	
		
		data.forEach(x->{
			
		
			Object value = ObjectUtils.getFieldValue(x, manyToMany.joinTableField());
			
			try {
				
				List<?> values = dataMap.get(value).stream().map(v->convert.convert(v,convertType)).collect(Collectors.toList());
				pd.getWriteMethod().invoke(x, values);
			
			} catch (Exception e) {
				throw new RuntimeException(e);
			} 
			
		});
		
		
	}

}
