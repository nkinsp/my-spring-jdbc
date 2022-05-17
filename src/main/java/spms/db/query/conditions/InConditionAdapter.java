package spms.db.query.conditions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import spms.db.annotation.query.In;
import spms.db.query.Query;

public class InConditionAdapter extends AbstractConditionAdapter<In>{

	@Override
	<T> Consumer<Query<T>> adapter(String name, Object value) {
		
		List<Object> params = new ArrayList<Object>();
		
		if(value.getClass().isArray()) {
			
			Object[] values = (Object[]) value;
			for (Object objectValue : values) {
				params.add(objectValue);
			}
			
		}
		if(value.getClass() == List.class || List.class.isAssignableFrom(value.getClass())) {
			List<?> values = (List<?>) value;
			params.addAll(values);
		}
	
		return x->x.in(name, params.toArray());
	}

	

	

	

}
