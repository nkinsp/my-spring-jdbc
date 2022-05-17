package spms.db.query.conditions;

import java.util.function.Consumer;


import spms.db.annotation.query.Lt;
import spms.db.query.Query;

public class LtConditionAdapter extends AbstractConditionAdapter<Lt>{

	@Override
	<T> Consumer<Query<T>> adapter(String name, Object value) {
		return x->x.lt(name, value);
	}

	

	

	

}
