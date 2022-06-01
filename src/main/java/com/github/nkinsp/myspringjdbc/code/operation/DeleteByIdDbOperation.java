package com.github.nkinsp.myspringjdbc.code.operation;

import com.github.nkinsp.myspringjdbc.cache.CacheManager;
import com.github.nkinsp.myspringjdbc.query.Query;

public class DeleteByIdDbOperation<T> extends DeleteDbOperation<T>{

	private Object id;
	
	public DeleteByIdDbOperation(Query<T> query,Object id) {
		super(query);
		this.id = id;
		this.query.where().idEq(id);
		
	
		
	}

	
	@Override
	public Object dbAdapter() {
		// TODO Auto-generated method stub
		if (tableMapping.isCache()) {

			CacheManager manager = context.getCacheManager();

			if (manager != null) {

				manager.delete(manager.getCacheKeyGenerated().generated(tableMapping.getTableClass(), id));
			}
		}

		return super.dbAdapter();

	}
}
