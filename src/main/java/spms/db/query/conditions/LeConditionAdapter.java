package spms.db.query.conditions;

import java.util.function.Consumer;

import spms.db.annotation.query.Le;
import spms.db.query.Query;

public class LeConditionAdapter extends AbstractConditionAdapter<Le>{

	@Override
	<T> Consumer<Query<T>> adapter(String name, Object value) {
		return x->x.le(name, value);
	}

	

	

	

}
