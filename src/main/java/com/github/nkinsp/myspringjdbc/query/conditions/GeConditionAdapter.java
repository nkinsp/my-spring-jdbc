package com.github.nkinsp.myspringjdbc.query.conditions;

import java.util.function.Consumer;

import com.github.nkinsp.myspringjdbc.annotation.query.Ge;
import com.github.nkinsp.myspringjdbc.query.Query;

public class GeConditionAdapter extends AbstractConditionAdapter<Ge>{

	@Override
	<T> Consumer<Query<T>> adapter(String name, Object value) {
		// TODO Auto-generated method stub
		return x->x.ge(name, value);
	}

	

	

	

}
