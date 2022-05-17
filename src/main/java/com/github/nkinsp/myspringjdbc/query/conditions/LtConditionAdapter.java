package com.github.nkinsp.myspringjdbc.query.conditions;

import java.util.function.Consumer;

import com.github.nkinsp.myspringjdbc.annotation.query.Lt;
import com.github.nkinsp.myspringjdbc.query.Query;

public class LtConditionAdapter extends AbstractConditionAdapter<Lt>{

	@Override
	<T> Consumer<Query<T>> adapter(String name, Object value) {
		return x->x.lt(name, value);
	}

	

	

	

}
