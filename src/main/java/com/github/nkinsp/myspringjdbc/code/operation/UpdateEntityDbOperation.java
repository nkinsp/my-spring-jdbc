package com.github.nkinsp.myspringjdbc.code.operation;


import com.github.nkinsp.myspringjdbc.query.Query;
import com.github.nkinsp.myspringjdbc.util.EntityUtils;

public class UpdateEntityDbOperation<T> extends UpdateEntityMapDbOperation<T>{

	
	public UpdateEntityDbOperation(Query<T> query, T model) {
		super(query, EntityUtils.entityToMap(model));
		// TODO Auto-generated constructor stub
	}
	
	

}
