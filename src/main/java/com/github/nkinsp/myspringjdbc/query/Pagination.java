package com.github.nkinsp.myspringjdbc.query;

public interface Pagination extends ConditionQuery {

	
	Integer getPageNo();
	
	Integer getPageSize();
}
