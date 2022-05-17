package com.github.nkinsp.myspringjdbc.query.conditions;

import java.util.function.Consumer;

import com.github.nkinsp.myspringjdbc.annotation.query.Le;
import com.github.nkinsp.myspringjdbc.query.Query;

public class LeConditionAdapter extends AbstractConditionAdapter<Le>{

	@Override
	<T> Consumer<Query<T>> adapter(String name, Object value) {
		return x->x.le(name, value);
	}

	

	

	

}
