package com.github.nkinsp.myspringjdbc.cache;

import org.springframework.util.DigestUtils;

public class DefaultCacheKeyGenerated implements CacheKeyGenerated{

	@Override
	public Object generated(Class<?> typeClass, Object value) {
		
		StringBuilder  builder = new StringBuilder();
		builder.append("cache:")
				   .append(typeClass.getSimpleName().toLowerCase())
				   .append(":")
				   .append(DigestUtils.md5DigestAsHex(value.toString().getBytes()));
		
		return builder.toString();
	}

}
