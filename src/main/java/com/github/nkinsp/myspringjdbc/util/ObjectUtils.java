package com.github.nkinsp.myspringjdbc.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectUtils {

	
	public static boolean isEmpty(Object object) {
		
		if(object == null) {
			return true;
		}
		
		if("".equals(object)) {
			return true;
		}
		
		if (object instanceof Map) {
			if (((Map<?, ?>) object).isEmpty()) {
				return true;
			}
		}
		if (object instanceof Collection) {
			if (((Collection<?>) object).isEmpty()) {
				return true;
			}
		}

		if (object.getClass().isArray()) {
			Object[] arr = (Object[]) object;
			if (arr.length == 0) {
				return true;
			}
		}
		return false;
		
	}
	
	@SuppressWarnings("unchecked")
	public static <T,En>  List<T> getFieldValues(List<En> ens,String filedName){
	
		
		return ens.stream().map(x->(T)getFieldValue(x, filedName)).filter(v->v != null).distinct().collect(Collectors.toList());
		
	}
	
	@SuppressWarnings("unchecked")
	public static <V,R>  R getFieldValue( V target,String filedName){
		 PropertyDescriptor pd = ClassUtils.findPropertyDescriptor(filedName, target.getClass());
			if(pd != null) {
				try {
					return (R)pd.getReadMethod().invoke(target);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
	}
	
}
