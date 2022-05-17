package com.github.nkinsp.myspringjdbc.code.filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.util.CollectionUtils;

import com.github.nkinsp.myspringjdbc.annotation.Select;
import com.github.nkinsp.myspringjdbc.code.AnnotationOperation;
import com.github.nkinsp.myspringjdbc.code.DbContext;
import com.github.nkinsp.myspringjdbc.table.TableMapping;

public class SelectSqlAnnontationOperationFilter implements  AnnotationOperation{

	private static Class<?>[] baseClassArrays = {
			int.class,
			Integer.class,
			long.class,
			Long.class,
			short.class,
			Short.class,
			BigDecimal.class,
			byte.class,
			Byte.class,
			boolean.class,
			Boolean.class,
			String.class,
			Date.class,
			java.util.Date.class,
			LocalDate.class
			
	};
	
	
	
    private boolean isObjectResult(Class<?> resultClass) {
    	
    	for (Class<?> clasz : baseClassArrays) {
			
    		if(clasz.equals(resultClass)) {
    			return false;
    		}
    		
		}
    	
    	return true;
    }
	
   private Class<?> getFirstReturnClass(Type type){
	   
	   
	   return (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
   }
   
   
   private <R> List<R> executeSqlQuery(DbContext dbContext,Class<R> classType,String sql,Object...params){
	   
	   if(isObjectResult(classType)) {
		   if(Map.class.equals(classType) || Map.class.isAssignableFrom(classType)) {
			   return dbContext.queryForList(sql, classType, params);
			   
		   }
		   return  dbContext.query(sql, new BeanPropertyRowMapper<>(classType),params);
	   }
	   
	   return dbContext.queryForList(sql, classType, params);
	   
   }
   
   
	@SuppressWarnings("unchecked")
	@Override
	public <R> R execute(Method method, DbContext dbContext, TableMapping<?> table) {
		// TODO Auto-generated method stub
		
		Select select = method.getAnnotation(Select.class);
		
		
		
		Class<?> returnType = method.getReturnType();
		
	
		
		//返回list
		if(returnType.equals(List.class)) {
			Class<?> firstReturnClassType = getFirstReturnClass(method.getAnnotatedReturnType().getType());
			return (R) executeSqlQuery(dbContext, firstReturnClassType, select.value(), "");
			
		}
		
		List<?> list = executeSqlQuery(dbContext, returnType, select.value(), "");
		
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
		
		return (R) list.get(0);
		
	}

	@Override
	public boolean match(Class<? extends Annotation> annotationClass) {
		// TODO Auto-generated method stub
		return annotationClass.equals(Select.class);
	}

	

}
