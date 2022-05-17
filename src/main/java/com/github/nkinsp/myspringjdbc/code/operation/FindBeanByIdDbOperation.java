package com.github.nkinsp.myspringjdbc.code.operation;

import com.github.nkinsp.myspringjdbc.query.Query;

public class FindBeanByIdDbOperation<T> extends FindByIdDbOperation<T, T>{

	
	public FindBeanByIdDbOperation(Query<T> query,Object id) {
		super(query, query.getTableMapping().getTableClass(),id);
		
	}

}
