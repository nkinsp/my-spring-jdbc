package com.github.nkinsp.myspringjdbc.code.repository;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.github.nkinsp.myspringjdbc.code.DbContext;
import com.github.nkinsp.myspringjdbc.code.operation.FindBeanListDbOperation;
import com.github.nkinsp.myspringjdbc.code.operation.FindMapListDbOperation;
import com.github.nkinsp.myspringjdbc.code.operation.FindObjectDbOperation;
import com.github.nkinsp.myspringjdbc.code.operation.PagingDbOperation;
import com.github.nkinsp.myspringjdbc.code.operation.UpdateDbOperation;
import com.github.nkinsp.myspringjdbc.query.ConditionQuery;
import com.github.nkinsp.myspringjdbc.query.Page;
import com.github.nkinsp.myspringjdbc.query.PaginationConditionQuery;
import com.github.nkinsp.myspringjdbc.query.Query;

public interface QueryRepository<T,Id> extends CrudReposotory<T,Id>{

	
	/**
	 * WHERE
	 * @param where
	 * @param params
	 * @return
	 */
	default Query<T> where(String where,Object...params) {
		return createQuery().where(where, params);
	}
	
	/**
	 * SELECT COUNT(1 )
	 * @param consumer
	 * @return
	 */
	default Integer findCount(Consumer<Query<T>> consumer) {
				
		return findObject(Integer.class, consumer.andThen(x->x.count("1")));
		
	}
	
	/**
	 * @param <R>
	 * @param typeClass
	 * @param consumer
	 * @return
	 */
	default <R> R findObject(Class<R> typeClass,Consumer<Query<T>> consumer) {
		
		Query<T> query = createQuery();
		consumer.accept(query);
		return execute(new FindObjectDbOperation<T, R>(query, typeClass));
		
	}
	
	/**
	 * find list
	 * @param consumer
	 * @return
	 */
	default List<T> findList(Consumer<Query<T>> consumer){
		Query<T> query = createQuery();
		consumer.accept(query);
		return execute(new FindBeanListDbOperation<>(query, query.getTableMapping().getTableClass()));
	}
	
	/**
	 * 获取所有数据
	 * @return
	 */
	default List<T> findAll(){
		
		return findList(x->{});
	}
	
	/**
	 * 分页查询
	 * @param <En>
	 * @param pageNo
	 * @param pageSize
	 * @param consumer
	 * @return
	 */
	default <En >List<T> findList(int pageNo,int pageSize,Consumer<Query<T>> consumer){
		
		Query<T> query = createQuery();
		consumer.accept(query);
		return execute(new PagingDbOperation<>(query, query.getTableMapping().getTableClass(), pageNo, pageSize));
		
	}
	
	
	/**
	 * 分页查询
	 * @param <En>
	 * @param pageNo
	 * @param pageSize
	 * @param entityClass
	 * @param consumer
	 * @return
	 */
	default <En > List<En> findList(int pageNo,int pageSize,Class<En> entityClass,Consumer<Query<T>> consumer){
		
		
		Query<T> query = createQuery();
		consumer.accept(query);
		
		return execute(new PagingDbOperation<>(query, entityClass, pageNo, pageSize));
		
	}
	
	/**
	 * 获取数据指定返回对象
	 * @param <En>
	 * @param entityClass
	 * @param consumer
	 * @return
	 */
	default <En> List<En> findList(Class<En> entityClass,Consumer<Query<T>> consumer){
		
		Query<T> query = createQuery();
		consumer.accept(query);
		return execute(new FindBeanListDbOperation<>(query, entityClass));
	}
	
	/**
	 * 实体条件查询
	 * @param <En>
	 * @param conditionQuery
	 * @param entityClass
	 * @return
	 */
	default <En> List<En> findList(ConditionQuery conditionQuery,Class<En> entityClass){
		
		return findList(entityClass,query->{
			getDbContext().executeConditionAdapter(conditionQuery, query);
			conditionQuery.then(query);
		});
		
		
		
	}
	
	/**
	 * @param conditionQuery
	 * @return
	 */
	default List<T> findListByConditions(ConditionQuery conditionQuery){
		
		return findList(query->{
			getDbContext().executeConditionAdapter(conditionQuery, query);
			conditionQuery.then(query);
		 
		});
	}
	
	default <En> List<En> findListByConditions(Class<En> enClass,ConditionQuery conditionQuery){
		
		return findList(enClass,query->{
			getDbContext().executeConditionAdapter(conditionQuery, query);
			conditionQuery.then(query);
		});
	}
	
	/**
	 * 
	 * @param conditionQuery
	 * @return
	 */
	default List<T> findListByPagConditions(PaginationConditionQuery conditionQuery){
		
		return findListByConditions(conditionQuery.getPageNo(), conditionQuery.getPageSize(), conditionQuery);
	}
	
	default <En> List<En> findListByPagConditions(Class<En> enClass,PaginationConditionQuery conditionQuery){
		
		return findListByConditions(enClass,conditionQuery.getPageNo(), conditionQuery.getPageSize(), conditionQuery);
	}
	
	/**
	 * 条件分页查询
	 * @param pageNo
	 * @param pageSize
	 * @param conditionQuery
	 * @return
	 */
	default List<T> findListByConditions(Integer pageNo,Integer pageSize,ConditionQuery conditionQuery){
		
	
		
		return findList(pageNo,pageSize,query->{
		      getDbContext().executeConditionAdapter(conditionQuery, query);
		      conditionQuery.then(query);
		});
	}
	
	
	
	/**
	 * 条件分页查询
	 * @param pageNo
	 * @param pageSize
	 * @param conditionQuery
	 * @return
	 */
	default <En> List<En> findListByConditions(Class<En> enClass,Integer pageNo,Integer pageSize,ConditionQuery conditionQuery){
		
	
		
		return findList(pageNo,pageSize,enClass,query->{
		      getDbContext().executeConditionAdapter(conditionQuery, query);
		      conditionQuery.then(query);
		});
	}
	
	default <En> List<En> findListByConditions(Class<En> enClass,Consumer<Query<T>> consumer,ConditionQuery conditionQuery){
		
	
		
		return findList(enClass,query->{
			  consumer.accept(query);
		      getDbContext().executeConditionAdapter(conditionQuery, query);
		      conditionQuery.then(query);
		});
	}
	
	/**
	 * 根据条件查询
	 * @param conditionQuery
	 * @return
	 */
	default Integer findCountByConditions(ConditionQuery conditionQuery) {
		
		return findCount(query->getDbContext().executeConditionAdapter(conditionQuery, query));
		
	}
	
	/**
	 * 分页查询
	 * @param pagination
	 * @return
	 */
	default Page<T> getPageByConditions(PaginationConditionQuery pagination){
		
		return new Page<T>(findCountByConditions(pagination),findListByConditions(pagination.getPageNo(),pagination.getPageSize(),pagination));
		
		
	}
	
	/**
	 * 数据更新
	 * @param consumer
	 * @return
	 */
	default Integer updateBy(Consumer<Query<T>> consumer) {
		
		Query<T> query = createQuery();
		consumer.accept(query);
		return execute(new UpdateDbOperation<>(query));
	}

	/**
	 * 返回map list
	 * @param consumer
	 * @return
	 */
	default List<Map<String,Object>>  findMapList(Consumer<Query<T>> consumer){
		
		Query<T> query = createQuery();
		consumer.accept(query);
		return execute(new FindMapListDbOperation<T>(query));
		
	}
	
	
	/**
	 * 获取DbContext
	 * @return
	 */
	DbContext getDbContext();
	
}
