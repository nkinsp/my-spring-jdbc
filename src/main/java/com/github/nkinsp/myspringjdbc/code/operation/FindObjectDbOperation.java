package com.github.nkinsp.myspringjdbc.code.operation;

import java.util.List;

import com.github.nkinsp.myspringjdbc.query.Query;


public class FindObjectDbOperation<T,R> extends FindObjectListDbOperation<T, R>{

	public FindObjectDbOperation(Query<T> query, Class<R> resultType) {
		super(query, resultType);
	}

	

	@SuppressWarnings("unchecked")
	@Override
	public Object dbAdapter() {
		List<R> results =  (List<R>) super.dbAdapter();
		if(results == null || results.size() == 0) {
			return null;
		}
		return results.get(0);
	}
	
	
	
}
