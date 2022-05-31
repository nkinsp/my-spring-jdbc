package com.github.nkinsp.myspringjdbc.query;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import com.github.nkinsp.myspringjdbc.code.DbContext;

public interface CascadeEntityAdapter<An> {

	boolean support(Class<? extends Annotation> annotationClass);
	
	<T> void adapter(List<T> data,Class<T> enClass,Field field,Annotation an,DbContext dbContext);
}
