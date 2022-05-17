package com.github.nkinsp.myspringjdbc.annotation.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.nkinsp.myspringjdbc.query.converts.DefaultConvert;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Gt {

	String value() default "";
	
	Class<?> convert() default DefaultConvert.class;
}
