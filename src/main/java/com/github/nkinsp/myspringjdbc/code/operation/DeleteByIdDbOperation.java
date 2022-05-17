package com.github.nkinsp.myspringjdbc.code.operation;

import com.github.nkinsp.myspringjdbc.query.Query;

public class DeleteByIdDbOperation<T> extends DeleteDbOperation<T>{

	public DeleteByIdDbOperation(Query<T> query,Object id) {
		super(query);
		this.query.where().idEq(id);
		
	
		
	}

}
