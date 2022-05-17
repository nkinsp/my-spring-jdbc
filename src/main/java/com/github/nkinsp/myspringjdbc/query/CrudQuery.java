package com.github.nkinsp.myspringjdbc.query;

import java.util.List;
import java.util.Map;

public interface CrudQuery<T> {

	
	/**
	 * 查询返回一条数据
	 * @return
	 */
	T find();
	
	/**
	 * 查询返回唯一结果
	 * @param <R>
	 * @param resultClass
	 * @return
	 */
	<R> R findUnique(Class<R> resultClass);
	
	/**
	 * 
	 * @param <En>
	 * @param enClass
	 * @return
	 */
	<En> En find(Class<En> enClass);
	
	/**
	 * 查询返回多条数据
	 * @return
	 */
	List<T> findList();
	
	/**
	 * 查询返回 实体对像List
	 * @param <En>
	 * @param enClass
	 * @return
	 */
	<En> List<En> findList(Class<En> enClass);
	
	
	/**
	 * 分页查询
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<T> findList(int pageNo,int pageSize);
	
	/**
	 * 分页查询 返回自定义实体对象List
	 * @param <En>
	 * @param pageNo
	 * @param pageSize
	 * @param enClass
	 * @return
	 */
	<En> List<En> findList(int pageNo,int pageSize,Class<En> enClass);
	
	/**
	 * 删除
	 * @return
	 */
	int delete();
	
	
	/**
	 * 更新
	 * @return
	 */
	int update();
	
	
	
	
}
