package com.github.nkinsp.myspringjdbc.code.operation;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.util.CollectionUtils;

import com.github.nkinsp.myspringjdbc.annotation.CascadeEntity;
import com.github.nkinsp.myspringjdbc.cache.CacheKeyGenerated;
import com.github.nkinsp.myspringjdbc.cache.CacheManager;
import com.github.nkinsp.myspringjdbc.query.Query;
import com.github.nkinsp.myspringjdbc.util.ClassUtils;
import com.github.nkinsp.myspringjdbc.util.ObjectUtils;

public class FindListByIdsDbOperation<T,En> extends FindBeanListDbOperation<T,T> {

	
	private Object[] ids;
	
	private Class<En> enClass;
	
	public FindListByIdsDbOperation(Query<T> query,Class<En> enClass,Object[] ids) {
		super(query,query.getTableMapping().getTableClass());
		this.ids = ids;
		this.enClass = enClass;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<T>  fromDb(Object[] ids) {
		this.query.where().idIn(ids);
		 List<T>  list =  (List<T>) super.dbAdapter();
		 
		 
		 return list;
		 
		 
		
	}
	
	@SuppressWarnings("unchecked")
	public List<T> fromCache(){
		
		CacheManager manager = context.getCacheManager();
		
		if(manager == null) {
			return fromDb(ids);
		}
		
		CacheKeyGenerated keyGenerated = manager.getCacheKeyGenerated();
		
		Class<T> tableClass = tableMapping.getTableClass();
		

		
	    List<Object> keys = Stream.of(ids).distinct().map(x->keyGenerated.generated(tableClass, x)).collect(Collectors.toList());
		
		List<Object> fromCacheValues = manager.multiGet(keys).stream().filter(x->!ObjectUtils.isEmpty(x)).collect(Collectors.toList());
		
		if(keys.size() == fromCacheValues.size()) {
			
			return (List<T>) fromCacheValues;
		}
		
		List<Object> results = new ArrayList<Object>();
		results.addAll(fromCacheValues);
		//缓存的key
		Set<Object> fromCahceKeySet = fromCacheValues.stream()
				.map(x->tableMapping.getIdValue(x))
				.collect(Collectors.toSet());
		
		List<Object> noCacheIds = Stream.of(ids).distinct().filter(x->!fromCahceKeySet.contains(x)).collect(Collectors.toList());
		
		if(!CollectionUtils.isEmpty(noCacheIds)) {
			List<T> fromDbValues = fromDb(noCacheIds.toArray());
			//添加缓存
			Map<Object, Object> map = fromDbValues.stream().collect(Collectors.toMap(x->keyGenerated.generated(tableClass, tableMapping.getIdValue(x)), v->v));
			manager.multiSet(map, tableMapping.getCacheTime(), TimeUnit.SECONDS);
			results.addAll(fromDbValues);
		}
	
		
		
		return (List<T>) results;
		
	}
	
	
	@Override
	public Object dbAdapter() {
		List<T> list = tableMapping.isCache() ? fromCache() : fromDb(ids);

		if (enClass.equals(tableMapping.getTableClass())) {
			return list;
		}

		List<En> result = list.stream().map(x -> ObjectUtils.copy(enClass, x)).collect(Collectors.toList());

		if (!ClassUtils.hasAnnotation(enClass, CascadeEntity.class)) {
			return list;
		}
		return context.executeCascadeEntityAdapter(result, tableMapping, enClass);

	}

	

	


}
