package com.github.nkinsp.myspringjdbc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.nkinsp.myspringjdbc.query.SelectColumnMapperRender;
import com.github.nkinsp.myspringjdbc.query.select.DefaultSelectColumnMapperRender;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SelectColumnMapper {

	
	String value() default "";
	
	Class<? extends SelectColumnMapperRender> render() default DefaultSelectColumnMapperRender.class;
	
}
