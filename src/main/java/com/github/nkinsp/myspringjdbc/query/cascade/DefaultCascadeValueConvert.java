package com.github.nkinsp.myspringjdbc.query.cascade;


import org.springframework.beans.BeanUtils;

import com.github.nkinsp.myspringjdbc.query.CascadeValueConvert;

public class DefaultCascadeValueConvert  implements CascadeValueConvert{

	@SuppressWarnings("unchecked")
	@Override
	public <V, R> R convert(V v, Class<R> typeClass) {
		
		if(v.getClass().equals(typeClass)) {
			return (R) v;
		}
		
		 R bean = BeanUtils.instantiate(typeClass);
		 BeanUtils.copyProperties(v, bean);
		 return bean;
		 
	}

	

	

	

	

}
