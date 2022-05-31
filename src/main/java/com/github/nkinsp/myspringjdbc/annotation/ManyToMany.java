package com.github.nkinsp.myspringjdbc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.nkinsp.myspringjdbc.query.CascadeValueConvert;
import com.github.nkinsp.myspringjdbc.query.cascade.DefaultCascadeValueConvert;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ManyToMany {

	
	Class<?> joinTableClass();
	
	String joinField();
	
	String joinTableField();
	
   Class<? extends CascadeValueConvert> convert() default DefaultCascadeValueConvert.class;
  
	
	
}
