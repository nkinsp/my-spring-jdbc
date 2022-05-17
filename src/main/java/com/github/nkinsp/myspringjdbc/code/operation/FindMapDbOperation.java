package com.github.nkinsp.myspringjdbc.code.operation;

import java.util.List;
import java.util.Map;

import com.github.nkinsp.myspringjdbc.query.Query;



public class FindMapDbOperation<T> extends FindMapListDbOperation<T>{

	public FindMapDbOperation(Query<T> query) {
		super(query);
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object dbAdapter() {
		// TODO Auto-generated method stub
		List<Map>  list = (List<Map>) super.dbAdapter();
		if(list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
}
