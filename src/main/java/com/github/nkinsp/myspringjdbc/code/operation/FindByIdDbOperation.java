package com.github.nkinsp.myspringjdbc.code.operation;

import com.github.nkinsp.myspringjdbc.query.Query;

public class FindByIdDbOperation<T,En> extends FindDbOperation<T, En>{

	
	public FindByIdDbOperation(Query<T> query, Class<En> enClass,Object id) {
		super(query, enClass);
		this.query.where().idEq(id);
		
	}

}
