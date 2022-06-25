package com.github.nkinsp.myspringjdbc.query.conditions;

import java.util.function.Consumer;

import com.github.nkinsp.myspringjdbc.query.Query;

public class QueryConditionAdapter extends AbstractConditionAdapter<com.github.nkinsp.myspringjdbc.annotation.query.Query>{

	@Override
	<T> Consumer<Query<T>> adapter(String name, Object value) {
		
		System.out.println("name=>"+name+"  value=>"+value);
		// TODO Auto-generated method stub
		return x->{
			x.addCondition(name, value);
		};
	}

	
}
