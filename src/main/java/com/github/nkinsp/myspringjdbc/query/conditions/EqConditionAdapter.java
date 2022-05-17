package com.github.nkinsp.myspringjdbc.query.conditions;

import java.util.function.Consumer;

import com.github.nkinsp.myspringjdbc.annotation.query.Eq;
import com.github.nkinsp.myspringjdbc.query.Query;

public class EqConditionAdapter extends AbstractConditionAdapter<Eq>{

	@Override
	<T> Consumer<Query<T>> adapter(String name, Object value) {
		// TODO Auto-generated method stub
		return x->{
			x.eq(name, value);
		};
	}

	

	

	
}
