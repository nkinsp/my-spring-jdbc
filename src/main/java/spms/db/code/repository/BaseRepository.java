package spms.db.code.repository;

import spms.db.code.DbOperation;
import spms.db.query.Query;

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
