package spms.db.query.conditions;

import java.util.function.Consumer;


import spms.db.query.Query;

public class NeConditionAdapter extends AbstractConditionAdapter<spms.db.annotation.query.Query>{

	@Override
	<T> Consumer<Query<T>> adapter(String name, Object value) {
		// TODO Auto-generated method stub
		return x->x.ne(name, value);
	}

	

	


}
