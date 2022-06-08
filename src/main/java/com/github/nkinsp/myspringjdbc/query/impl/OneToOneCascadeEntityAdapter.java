package com.github.nkinsp.myspringjdbc.query.impl;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.github.nkinsp.myspringjdbc.annotation.OneToOne;
import com.github.nkinsp.myspringjdbc.code.DbContext;
import com.github.nkinsp.myspringjdbc.code.repository.QueryRepository;
import com.github.nkinsp.myspringjdbc.query.CascadeEntityAdapter;
import com.github.nkinsp.myspringjdbc.query.CascadeValueConvert;
import com.github.nkinsp.myspringjdbc.table.TableMapping;
import com.github.nkinsp.myspringjdbc.util.ClassUtils;
import com.github.nkinsp.myspringjdbc.util.ObjectUtils;

public class OneToOneCascadeEntityAdapter implements CascadeEntityAdapter<OneToOne>{

	
	@Override
	public boolean support(Class<? extends Annotation> annotationClass) {
		// TODO Auto-generated method stub
		return annotationClass.equals(OneToOne.class);
	}

	@Override
	public <T> void adapter(List<T> data, TableMapping<?> tableMapping,Class<T> enClass, Field field, Annotation annotation,
			DbContext dbContext) {

		if(CollectionUtils.isEmpty(data)) {
			return;
		}
		
		OneToOne manyToOne = (OneToOne) annotation;

		List<Object> fieldValues = ObjectUtils.getFieldValues(data, manyToOne.joinField()).stream().distinct().collect(Collectors.toList());
		
		if(CollectionUtils.isEmpty(fieldValues)) {
			return;
		}
		

		QueryRepository<?, Object> repository = dbContext.table(manyToOne.joinTableClass());

		String idFieldName = dbContext.findTableMapping(manyToOne.joinTableClass()).getIdProperty().getFieldName();

		CascadeValueConvert convert = ClassUtils.newInstance(manyToOne.convert());

		Map<Object, Object> dataMap = repository.findList(fieldValues).stream().collect(Collectors
				.toMap(x -> ObjectUtils.getFieldValue(x, idFieldName), v -> convert.convert(v, field.getType())));

		PropertyDescriptor pd = ClassUtils.findPropertyDescriptor(field.getName(), enClass);

		data.forEach(x -> {

			Object fieldValue = ObjectUtils.getFieldValue(x, manyToOne.joinField());

			Object object = dataMap.get(fieldValue);

			try {
				pd.getWriteMethod().invoke(x, object);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		});
	}


	

	

}
