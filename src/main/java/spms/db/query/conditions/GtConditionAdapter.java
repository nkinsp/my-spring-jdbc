package spms.db.query.conditions;

import java.util.function.Consumer;


import spms.db.annotation.query.Gt;
import spms.db.query.Query;

public class GtConditionAdapter extends AbstractConditionAdapter<Gt>{

	@Override
	<T> Consumer<Query<T>> adapter(String name, Object value) {
		return x->x.gt(name, value);
	}

	

	

	

}
