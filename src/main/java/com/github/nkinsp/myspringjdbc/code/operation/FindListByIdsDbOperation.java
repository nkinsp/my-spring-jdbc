package com.github.nkinsp.myspringjdbc.code.operation;


import com.github.nkinsp.myspringjdbc.query.Query;

public class FindListByIdsDbOperation<T,En> extends FindBeanListDbOperation<T,En> {

	
	
	public FindListByIdsDbOperation(Query<T> query,Class<En> enClass,Object[] ids) {
		super(query,enClass);
		this.query.where().idIn(ids);
		
	}
	
	

	

	


}
