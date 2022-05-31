package com.github.nkinsp.myspringjdbc.code.repository;

import com.github.nkinsp.myspringjdbc.code.DbOperation;
import com.github.nkinsp.myspringjdbc.query.Query;

public interface BaseRepository<T> {

	
	Query<T> createQuery();
	
	
	
	
	default <R> R execute(DbOperation operation) {
		try {
			return operation.execute();
		} finally {
			operation = null;
		}
	}
}
