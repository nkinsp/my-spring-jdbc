package com.github.nkinsp.myspringjdbc.query;

import java.lang.annotation.Annotation;
import java.util.function.Consumer;

public interface ConditionAdapter<A extends Annotation> {

	
	boolean match(Class<? extends Annotation> annotationClass);
	
	
    <T> Consumer<Query<T>> adapter(ConditionField field);
	
}
