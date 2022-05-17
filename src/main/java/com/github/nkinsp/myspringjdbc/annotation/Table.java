package com.github.nkinsp.myspringjdbc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.nkinsp.myspringjdbc.table.IdType;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
	
	/**
	 * 表名
	 */
	String name() default "";
	 
	/**
	 * 主键名称
	 */
	String id() default "id";
	
	/**
	 * 主键类型
	 */
	IdType idType() default IdType.AUTO;
	
	
	/**
	 * 是否缓存
	 * @return
	 */
	boolean cache() default false;
	
	/**
	 * 缓存时间 单位秒
	 * @return
	 */
	long cacheTime() default -1;
	
	/**
	 * 逻辑删除
	 * @return
	 */
	boolean logicDeleteEnable() default false;
	
	/**
	 * 逻辑删除字段名称
	 * @return
	 */
	String logicDeleteName() default "del_flag";
	
	
	
}
