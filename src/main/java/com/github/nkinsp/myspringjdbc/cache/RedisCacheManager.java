package com.github.nkinsp.myspringjdbc.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.data.redis.serializer.RedisSerializer;

public class RedisCacheManager  implements CacheManager{

	private RedisTemplate<Object, Object> redisTemplate;

	@Override
	public Object get(Object key) {
	
		return redisTemplate.opsForValue().get(key);
	}
	

	@Override
	public void set(Object key, Object value, long timeout, TimeUnit unit) {
	
		if(timeout > 0) {
			redisTemplate.opsForValue().set(key, value, timeout, unit);
			return;
		}

		redisTemplate.opsForValue().set(key, value);
		
	}


	@Override
	public List<Object> multiGet(Collection<Object> keys) {
		
		return redisTemplate.opsForValue().multiGet(keys);
	}

    
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void multiSet(Map<Object, Object> data, long timeout, TimeUnit unit) {

		// 批量设置
		redisTemplate.opsForValue().multiSet(data);
		if (timeout > 0) {

			final RedisSerializer keySerializer = redisTemplate.getKeySerializer();
			List<byte[]> keys = data.keySet().stream().map(k -> keySerializer.serialize(k))
					.collect(Collectors.toList());
			long seconds = TimeoutUtils.toSeconds(timeout, unit);

			// 批量设置过期时间
			redisTemplate.executePipelined(new RedisCallback<Object>() {

				@Override
				public Object doInRedis(RedisConnection connection) throws DataAccessException {
					for (byte[] key : keys) {
						connection.expire(key, seconds);
					}
					return null;
				}
			});

		}

	}


	@Override
	public void delete(Object key) {
		redisTemplate.delete(key);
		
	}


	@Override
	public void delete(Collection<Object> keys) {
		redisTemplate.delete(keys);
		
	}


	@Override
	public CacheKeyGenerated getCacheKeyGenerated() {
		// TODO Auto-generated method stub
		return new DefaultCacheKeyGenerated();
	}


	public RedisCacheManager(RedisTemplate<Object, Object> redisTemplate) {
		
		this.redisTemplate = redisTemplate;
	}
	
	
}
