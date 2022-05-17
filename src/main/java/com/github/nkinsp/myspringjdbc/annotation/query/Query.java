package com.github.nkinsp.myspringjdbc.annotation.query;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.nkinsp.myspringjdbc.query.converts.DefaultConvert;

@Target({ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Query {

	
	String value() default "";
	
	Class<?> convert() default DefaultConvert.class;
	

	
}
