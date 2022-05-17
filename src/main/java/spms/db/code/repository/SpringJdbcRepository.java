package spms.db.code.repository;

import java.lang.reflect.ParameterizedType;

import org.springframework.beans.factory.annotation.Autowired;

import spms.db.code.DbContext;
import spms.db.query.Query;

public abstract class SpringJdbcRepository<T,Id> implements QueryRepository<T, Id>  {

	@Autowired
	private DbContext dbContext;
	
	
	private Class<T> tableClass;
	
	
	@SuppressWarnings("unchecked")
	public SpringJdbcRepository() {
		this.tableClass = (Class<T>) (((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}
	
	
	@Override
	public Query<T> createQuery() {
		
		return dbContext.createQuery(this.tableClass);
	}


	@Override
	public DbContext getDbContext() {
		
		return this.dbContext;
	}


	
	

}
