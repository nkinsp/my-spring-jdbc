package com.github.nkinsp.myspringjdbc.code.repository;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.nkinsp.myspringjdbc.code.DbContext;
import com.github.nkinsp.myspringjdbc.code.Rows;
import com.github.nkinsp.myspringjdbc.query.ConditionQuery;
import com.github.nkinsp.myspringjdbc.query.PaginationConditionQuery;
import com.github.nkinsp.myspringjdbc.query.Query;

public abstract  class SpringJdbcRepository<T,Id> implements QueryRepository<T, Id>,RowsRepository<T,Id>  {

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


	public <R,V> Map<R, V>  toMapByIds(List<Id> ids,Function<T, R> key,Function<T, V> value) {
		
		return findList(ids.stream().distinct().collect(Collectors.toList())).stream().collect(Collectors.toMap(key, value));
		
		
	}
	
	
	@Override
	public Class<T> getTableClass() {
		return this.tableClass;
	}


	@Override
	public <E> Rows<E> findRows(Class<E> entityClass, List<Id> ids) {
		
		return Rows.of(findList(ids, entityClass));
	}


	@Override
	public <E> Rows<E> findRows(Class<E> entiryClass, Consumer<Query<T>> query) {
		
		return Rows.of(findList(entiryClass,query));
	}


	


	@Override
	public <E> Rows<E> findRowsBy(Class<E> entityClass, ConditionQuery query) {
		
		return Rows.of(findListByConditions(entityClass, query));
	}


	@Override
	public <E> Rows<E> findRowsBy(Class<E> entityClass, PaginationConditionQuery query) {
		
		return Rows.of(findListByPagConditions(entityClass, query));
	}
	
	

}
