package com.github.nkinsp.myspringjdbc.query;



public interface CascadeValueConvert {

	
	<V,R> R convert(V v,Class<R> typeClass);
}
