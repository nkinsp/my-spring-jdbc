package com.github.nkinsp.myspringjdbc.query.converts;

import com.github.nkinsp.myspringjdbc.query.QueryConvert;

public class DefaultConvert implements QueryConvert<Object,Object>{

	@Override
	public Object convert(Object value) {
		return value;
	}

}
