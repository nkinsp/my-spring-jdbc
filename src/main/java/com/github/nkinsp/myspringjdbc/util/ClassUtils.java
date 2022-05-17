package com.github.nkinsp.myspringjdbc.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;


/**
 * 
 *
 */
public class ClassUtils {
	

	private static Map<String, Map<String,PropertyDescriptor>> pdCacheMap = new ConcurrentHashMap<>();
	
	private static Map<String, Map<String, Field>> fieldCacheMap = new ConcurrentHashMap<>();
	
	private static Map<String, Class<?>> classCacheMap = new ConcurrentHashMap<String, Class<?>>();
	
	public static Class<?> forName(String name) {
		Class<?> findClass = classCacheMap.get(name);
		if (findClass != null) {
			return findClass;
		}
		try {
			Class<?> forName = Class.forName(name);

			classCacheMap.put(name, forName);
			return forName;

		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public synchronized static Map<String, Field> getClassFieldMap(Class<?> beanClass) {
		Map<String, Field> cacheFieldMap = fieldCacheMap.get(beanClass.getName());
		if (cacheFieldMap != null) {
			return cacheFieldMap;
		}
		final Map<String, Field> fieldMap = new LinkedHashMap<String, Field>();

		forEachClassAllField(beanClass, field -> {
			fieldMap.put(field.getName(), field);
			return true;
		});
		fieldCacheMap.put(beanClass.getName(), fieldMap);
		return fieldMap;

	}
	
	
	public static boolean forEachClassAllField(Class<?> beanClass,Function<Field,Boolean> fieldCallback) {
		if(beanClass != Object.class) {
			boolean next = forEachClassAllField(beanClass.getSuperclass(), fieldCallback);
			if(!next) {
				return false;
			}
			
			Field[] fields = beanClass.getDeclaredFields();
			for (Field field : fields) {
				if(!fieldCallback.apply(field)) {
					return false;
				}
			}
			
		}
		return true;
	}

	
	public static PropertyDescriptor createPropertyDescriptor(Class<?> beanClass,String name) {
		try {
			PropertyDescriptor pd = new PropertyDescriptor(name, beanClass);
			if(pd.getWriteMethod() == null && pd.getReadMethod() == null) {
				return null;
			}
			return pd;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取字段
	 * @param name
	 * @param beanClass
	 * @return
	 */
	public static Field findField(String name,Class<?> beanClass) {
		Map<String, Field> dataMap = getClassFieldMap(beanClass);
		return dataMap.get(name);
	}
	
	
	public static synchronized Collection<PropertyDescriptor> getPropertyDescriptors(Class<?> beanClass) {
		Map<String, PropertyDescriptor> cachePdMap = pdCacheMap.get(beanClass.getName());
		if (cachePdMap != null) {
			return cachePdMap.values();
		}
		
		final Map<String, PropertyDescriptor> pdMap = new LinkedHashMap<String, PropertyDescriptor>();

		forEachClassAllField(beanClass, field -> {

			PropertyDescriptor pd = createPropertyDescriptor(beanClass, field.getName());
			if (pd != null) {
				pdMap.put(pd.getName(), pd);
			}
			return true;
		});
		pdCacheMap.put(beanClass.getName(), pdMap);
		return pdMap.values();

	}
	
	public static <T> T newInstance(Class<T> clasz) {
		try {
			return clasz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	
}
