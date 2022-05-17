package com.github.nkinsp.myspringjdbc.query;


public interface ConditionQuery {

	

	<T> void then(Query<T> query);
	
}
