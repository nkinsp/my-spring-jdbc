package com.github.nkinsp.myspringjdbc.util;

import java.util.Collection;
import java.util.Map;

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
}
