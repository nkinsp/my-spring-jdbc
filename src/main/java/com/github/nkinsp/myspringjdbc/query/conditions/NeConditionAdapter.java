package com.github.nkinsp.myspringjdbc.query.conditions;

import java.util.function.Consumer;

import com.github.nkinsp.myspringjdbc.query.Query;

public class NeConditionAdapter extends AbstractConditionAdapter<com.github.nkinsp.myspringjdbc.annotation.query.Query>{

	@Override
	<T> Consumer<Query<T>> adapter(String name, Object value) {
		// TODO Auto-generated method stub
		return x->x.ne(name, value);
	}

	

	


}
