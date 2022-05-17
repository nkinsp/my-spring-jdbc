package com.github.nkinsp.myspringjdbc.query;

public interface QueryConvert<T extends Object,R extends Object> {

	
	
	T convert(R value);
	
}
