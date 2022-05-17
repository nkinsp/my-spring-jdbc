package spms.db.query.conditions;

import java.util.function.Consumer;


import spms.db.annotation.query.Ge;
import spms.db.query.Query;

public class GeConditionAdapter extends AbstractConditionAdapter<Ge>{

	@Override
	<T> Consumer<Query<T>> adapter(String name, Object value) {
		// TODO Auto-generated method stub
		return x->x.ge(name, value);
	}

	

	

	

}
