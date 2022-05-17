package spms.db.annotation.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import spms.db.query.converts.DefaultConvert;

/**
 * like 
 * @author yue
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Like {

	String value() default "";
	
	Class<?> convert() default DefaultConvert.class;
	
	boolean prefix() default true;
	
	boolean suffix() default true;
}
