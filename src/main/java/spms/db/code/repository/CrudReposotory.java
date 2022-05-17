package spms.db.code.repository;

import java.util.List;

import spms.db.code.operation.DeleteByIdDbOperation;
import spms.db.code.operation.DeleteByIdsDbOperation;
import spms.db.code.operation.FindBeanByIdDbOperation;
import spms.db.code.operation.FindListByIdsDbOperation;
import spms.db.code.operation.InsertEntityBatchDbOperation;
import spms.db.code.operation.InsertEntityDbOperation;
import spms.db.code.operation.InsertOrUpdateDbOperation;
import spms.db.code.operation.LogicDeleteByIdDbOperation;
import spms.db.code.operation.LogicDeleteByIdsDbOperation;
import spms.db.code.operation.UpdateEntityBatchDbOperation;
import spms.db.code.operation.UpdateEntityDbOperation;
import spms.db.query.Query;


public interface CrudReposotory<T,Id>  extends BaseRepository<T>{

	
	/**
	 * 通过id获取
	 * @param id
	 * @return
	 */
	default T find(Id id) {
		
		return execute(new FindBeanByIdDbOperation<>(createQuery(),id));
	}
	
	/**
	 * 通过id获取多条数据
	 * @param ids
	 * @return
	 */
	default List<T> findList(List<Id> ids){
		
		return execute(new FindListByIdsDbOperation<>(createQuery(), ids.toArray()));
	}
	
	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	default int delete(Id id) {
		Query<T> query = createQuery();
		
		if(query.getTableMapping().isLogicDelete()) {
			return execute(new LogicDeleteByIdDbOperation<T>(query, id));
		}
		return execute(new DeleteByIdDbOperation<T>(query, id));
	}
	
	
	/**
	 * 批量删除
	 * @param ids
	 * @return 
	 */
	default void delete(List<Id> ids) {
		
		Query<T> query = createQuery();
		
		if(query.getTableMapping().isLogicDelete()) {
			execute(new LogicDeleteByIdsDbOperation<>(query,ids));
			return;
		}
		
		execute(new DeleteByIdsDbOperation<>(query, ids.toArray()));
		
		
	}
	
	/**
	 * 添加数据
	 * @param model
	 * @return
	 */
	default Id save(T model) {
		
		return execute(new InsertEntityDbOperation<T>(createQuery(), model));
		
	}
	
	/**
	 * 批量添加数据
	 * @param models
	 */
	default void saveBatch(List<T> models) {
	
		execute(new InsertEntityBatchDbOperation<T>(createQuery(), models));
	}
	
	/**
	 * 更新
	 * @param model
	 * @return
	 */
	default int update(T model) {
		
		return execute(new UpdateEntityDbOperation<T>(createQuery(), model));
	}
	
	/**
	 * 添加或者更新
	 * @param model
	 * @return
	 */
	default Id saveOrUpdate(T model) {
		
		return execute(new InsertOrUpdateDbOperation<T>(createQuery(), model));
	}
	
	/**
	 * 批量更新
	 * @param models
	 */
	default void updateBatch(List<T> models) {
		execute(new UpdateEntityBatchDbOperation<T>(createQuery(), models));
	}
	
	
}
