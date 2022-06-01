package com.github.nkinsp.myspringjdbc.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface CacheManager {

	Object get(Object key);
	
	void set(Object key,Object value,long timeout, TimeUnit unit);
	
	List<Object> multiGet(Collection<Object> keys);
	
	void multiSet(Map<Object, Object> data,long timeout, TimeUnit unit);
	
	void delete(Object key);
	
	void delete(Collection<Object> keys);
	
	CacheKeyGenerated getCacheKeyGenerated();
}
