package com.github.nkinsp.myspringjdbc.code;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.github.nkinsp.myspringjdbc.table.TableMapping;

public interface AnnotationOperation {

	
	
	boolean match(Class<? extends Annotation> annotationClass);
	
	<R> R execute(Method method,DbContext context,TableMapping<?> table); 
	
}
