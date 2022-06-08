package com.github.nkinsp.myspringjdbc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.nkinsp.myspringjdbc.query.CascadeValueConvert;
import com.github.nkinsp.myspringjdbc.query.cascade.DefaultCascadeValueConvert;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface OneToOne {

	
	Class<?> joinTableClass();
	
	String joinField();
	
    Class<? extends CascadeValueConvert>  convert() default DefaultCascadeValueConvert.class;
    
  
	
	
}
