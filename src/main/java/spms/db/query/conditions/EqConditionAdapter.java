package spms.db.query.conditions;

import java.util.function.Consumer;


import spms.db.annotation.query.Eq;
import spms.db.query.Query;

public class EqConditionAdapter extends AbstractConditionAdapter<Eq>{

	@Override
	<T> Consumer<Query<T>> adapter(String name, Object value) {
		// TODO Auto-generated method stub
		return x->{
			x.eq(name, value);
		};
	}

	

	

	
}
