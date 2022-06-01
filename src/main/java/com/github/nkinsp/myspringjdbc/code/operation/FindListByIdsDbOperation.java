package com.github.nkinsp.myspringjdbc.code.operation;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.util.CollectionUtils;

import com.github.nkinsp.myspringjdbc.cache.CacheKeyGenerated;
import com.github.nkinsp.myspringjdbc.cache.CacheManager;
import com.github.nkinsp.myspringjdbc.query.Query;
import com.github.nkinsp.myspringjdbc.util.ObjectUtils;

public class FindListByIdsDbOperation<T,En> extends FindBeanListDbOperation<T,En> {

	
	private Object[] ids;
	
	public FindListByIdsDbOperation(Query<T> query,Class<En> enClass,Object[] ids) {
		super(query,enClass);
		this.ids = ids;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<En>  fromDb(Object[] ids) {
		this.query.where().idIn(ids);
		return (List<En>) super.dbAdapter();
		
		
	}
	
	public Object fromCache(){
		
		CacheManager manager = context.getCacheManager();
		
		if(manager == null) {
			return super.dbAdapter();
		}
		
		CacheKeyGenerated keyGenerated = manager.getCacheKeyGenerated();
		
		Class<T> tableClass = tableMapping.getTableClass();
		

		
	    List<Object> keys = Stream.of(ids).distinct().map(x->keyGenerated.generated(tableClass, x)).collect(Collectors.toList());
		
		List<Object> fromCacheValues = manager.multiGet(keys).stream().filter(x->!ObjectUtils.isEmpty(x)).collect(Collectors.toList());
		
		if(keys.size() == fromCacheValues.size()) {
			
			return fromCacheValues;
		}
		
		List<Object> results = new ArrayList<Object>();
		results.addAll(fromCacheValues);
		//缓存的key
		Set<Object> fromCahceKeySet = fromCacheValues.stream()
				.map(x->tableMapping.getIdValue(x))
				.collect(Collectors.toSet());
		
		List<Object> noCacheIds = Stream.of(ids).distinct().filter(x->!fromCahceKeySet.contains(x)).collect(Collectors.toList());
		
		if(!CollectionUtils.isEmpty(noCacheIds)) {
			List<En> fromDbValues = fromDb(noCacheIds.toArray());
			//添加缓存
			Map<Object, Object> map = fromDbValues.stream().collect(Collectors.toMap(x->keyGenerated.generated(tableClass, tableMapping.getIdValue(x)), v->v));
			manager.multiSet(map, tableMapping.getCacheTime(), TimeUnit.SECONDS);
			results.addAll(fromDbValues);
		}
		
		return results;
		
	}
	
	
	@Override
	public Object dbAdapter() {
		return  tableMapping.isCache()?fromCache():fromDb(ids);
	}

	

	


}
