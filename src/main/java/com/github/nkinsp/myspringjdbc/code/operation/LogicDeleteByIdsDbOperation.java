package com.github.nkinsp.myspringjdbc.code.operation;

import java.util.List;

import com.github.nkinsp.myspringjdbc.query.Query;


public class LogicDeleteByIdsDbOperation<T,Id> extends UpdateDbOperation<T>{


	
	public LogicDeleteByIdsDbOperation(Query<T> query,List<Id> ids) {
		
		super(query);
		query.set(tableMapping.getLogicDeleteName(), 1).where().idIn(ids.toArray());
		
		
	}

	
	
	
	

}
