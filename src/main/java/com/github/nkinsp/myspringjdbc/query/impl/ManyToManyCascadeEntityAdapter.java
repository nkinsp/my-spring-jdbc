package com.github.nkinsp.myspringjdbc.query.impl;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.util.CollectionUtils;

import com.github.nkinsp.myspringjdbc.annotation.ManyToMany;
import com.github.nkinsp.myspringjdbc.code.DbContext;
import com.github.nkinsp.myspringjdbc.code.repository.QueryRepository;
import com.github.nkinsp.myspringjdbc.query.CascadeEntityAdapter;
import com.github.nkinsp.myspringjdbc.query.CascadeValueConvert;
import com.github.nkinsp.myspringjdbc.table.TableMapping;
import com.github.nkinsp.myspringjdbc.util.ClassUtils;
import com.github.nkinsp.myspringjdbc.util.ObjectUtils;

public class ManyToManyCascadeEntityAdapter implements CascadeEntityAdapter<ManyToMany>{

	
	private Stream<Object> getObjectToStream(Object x){
		
		  if(x instanceof String) {
			  return Stream.of(Arrays.asList(x.toString().split(",")).toArray());
		  }
		  if(x instanceof Collection) {
			  return Stream.of(	((Collection<?>) x).toArray());
		  }
		  if(x.getClass().isArray()) {
			  
			  return Stream.of((Object[])x);
			  
		  }
		  return Stream.of(x);
		
	}
	
	@Override
	public boolean support(Class<? extends Annotation> annotationClass) {
		// TODO Auto-generated method stub
		return annotationClass.equals(ManyToMany.class);
	}

	@Override
	public <T> void adapter(List<T> data, TableMapping<?> tableMapping, Class<T> enClass, Field field,
			Annotation annotation, DbContext dbContext) {

		if (CollectionUtils.isEmpty(data)) {
			return;
		}

		ManyToMany manyToMany = (ManyToMany) annotation;

		List<Object> fieldValues = ObjectUtils.getFieldValues(data, manyToMany.joinField()).stream()
				.filter(x->!ObjectUtils.isEmpty(x))
				.flatMap(x -> getObjectToStream(x)).distinct().collect(Collectors.toList());

		if (CollectionUtils.isEmpty(fieldValues)) {
			return;
		}

		QueryRepository<?, Object> repository = dbContext.table(manyToMany.joinTableClass());
		CascadeValueConvert convert = ClassUtils.newInstance(manyToMany.convert());

		ParameterizedType type = (ParameterizedType) field.getGenericType();
		Class<?> convertType = (Class<?>) type.getActualTypeArguments()[0];

		String joinTableIdField = repository.createQuery().getTableMapping().getIdProperty().getFieldName();

		Map<String, ?> dataMap = repository.findList(fieldValues).stream()
				.collect(Collectors.toMap(x -> ObjectUtils.getFieldValue(x, joinTableIdField).toString(), v -> v));

		PropertyDescriptor pd = ClassUtils.findPropertyDescriptor(field.getName(), enClass);

		data.forEach(x -> {

			Object value = ObjectUtils.getFieldValue(x, manyToMany.joinField());
			try {

				List<?> values = getObjectToStream(value).map(k->k.toString()).map(k -> dataMap.get(k)).filter(v -> !ObjectUtils.isEmpty(v))
						.map(v -> convert.convert(v, convertType)).collect(Collectors.toList());
				pd.getWriteMethod().invoke(x, values);

			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		});

	}

}
