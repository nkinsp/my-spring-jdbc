package com.github.nkinsp.myspringjdbc.code.operation;




import com.github.nkinsp.myspringjdbc.query.Query;

public class DeleteByIdsDbOperation<T> extends DeleteDbOperation<T>{


	public DeleteByIdsDbOperation(Query<T> query,final Object[] ids) {
		super(query);
		this.query.where().idIn(ids);
	}
	
	

}
