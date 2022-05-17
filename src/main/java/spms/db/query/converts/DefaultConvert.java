package spms.db.query.converts;

import spms.db.query.QueryConvert;

public class DefaultConvert implements QueryConvert<Object,Object>{

	@Override
	public Object convert(Object value) {
		return value;
	}

}
