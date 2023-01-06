package com.github.nkinsp.myspringjdbc.code.operation;

import java.util.concurrent.TimeUnit;

import com.github.nkinsp.myspringjdbc.cache.CacheManager;
import com.github.nkinsp.myspringjdbc.query.Query;

public class FindByIdDbOperation<T,En> extends FindDbOperation<T, En>{

	
	private Object id;
	
	
	public FindByIdDbOperation(Query<T> query, Class<En> enClass,Object id) {
		super(query, enClass);
		this.id = id;
		this.query.where().idEq(id);
		
	}
	
	
	@Override
	public Object dbAdapter() {
		
		if(!tableMapping.isCache()) {
			
			return super.dbAdapter();
		}
		
		CacheManager manager = context.getCacheManager();
		if(manager == null) {
			return super.dbAdapter();
		}
		
		Object key = manager.getCacheKeyGenerated().generated(tableMapping.getTableClass(), id);
		Object value = manager.get(key);
		if(value != null) {
			return value;
		}
		//lock
		synchronized (key.toString().intern()) {

			Object fromDb = super.dbAdapter();
			manager.set(key, fromDb, tableMapping.getCacheTime(), TimeUnit.SECONDS);

			return fromDb;
		}
	}
	

}
