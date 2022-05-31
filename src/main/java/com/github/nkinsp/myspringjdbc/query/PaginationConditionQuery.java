package com.github.nkinsp.myspringjdbc.query;

public interface PaginationConditionQuery extends ConditionQuery {

	
	Integer getPageNo();
	
	Integer getPageSize();
}
