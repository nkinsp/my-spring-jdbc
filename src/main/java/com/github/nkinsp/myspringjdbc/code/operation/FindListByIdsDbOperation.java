package com.github.nkinsp.myspringjdbc.code.operation;


import com.github.nkinsp.myspringjdbc.query.Query;

public class FindListByIdsDbOperation<T,En> extends FindBeanListDbOperation<T,Object> {

	
	
	@SuppressWarnings("unchecked")
	public FindListByIdsDbOperation(Query<T> query,Object[] ids) {
		super(query,(Class<Object>) query.getTableMapping().getTableClass());
		this.query.where().idIn(ids);
		
	}
	
	

	

	


}
