package com.github.nkinsp.myspringjdbc.code.operation;




import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.nkinsp.myspringjdbc.cache.CacheManager;
import com.github.nkinsp.myspringjdbc.query.Query;

public class DeleteByIdsDbOperation<T> extends DeleteDbOperation<T>{


	private Object[] ids;
	
	public DeleteByIdsDbOperation(Query<T> query,final Object[] ids) {
		super(query);
		this.ids = ids;
		this.query.where().idIn(ids);
	}
	
	
	@Override
	public Object dbAdapter() {
		// TODO Auto-generated method stub
		if (tableMapping.isCache()) {
			CacheManager manager = context.getCacheManager();

			if (manager != null) {

				List<Object> cacheKeys = Stream.of(ids)
						.map(x -> manager.getCacheKeyGenerated().generated(tableMapping.getTableClass(), x))
						.collect(Collectors.toList());

				manager.delete(cacheKeys);

			}
		}

		return super.dbAdapter();
	}
	

}
