package com.github.nkinsp.myspringjdbc.activerecord;


import java.lang.reflect.ParameterizedType;

import com.github.nkinsp.myspringjdbc.code.DbContext;
import com.github.nkinsp.myspringjdbc.code.repository.QueryRepository;
import com.github.nkinsp.myspringjdbc.query.Query;



/**
 * activerecord Model
 * @author hanjiang.Yue
 * @param <M>
 * @param <Id>
 */
@SuppressWarnings("unchecked")
public class Model<M,Id> implements QueryRepository<M, Id>{
	
	public transient static  DbContext dbContext = null;

	public transient Class<Id> idClass =  (Class<Id>) (((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
	
	
	public Model() {
		super();
	}
	

	/**
	 * 加载DbContent 
	 * @author hanjiang.Yue
	 * @param context
	 */
	public static void init(DbContext context) {
		dbContext = context;
	}
	


	protected M modelThis() {
		return (M) this;
	}
	
	
	
	/**
	 * 新增
	 * @author hanjiang.Yue
	 * @return
	 */
	public Id save() {
		return save(modelThis());
	}
	
	/**
	 * 删除
	 * @author hanjiang.Yue
	 * @return
	 */
	public int delete() {
		Id id  = dbContext.findTableMapping(this.getClass()).getIdProperty().getValue(this);
		return delete(id);
	}
	
	/**
	 * 更新
	 * @author hanjiang.Yue
	 * @return
	 */
	public int update() {
		return update(modelThis());
	}


	@Override
	public Query<M> createQuery() {
		// TODO Auto-generated method stub
		return (Query<M>) dbContext.createQuery(this.getClass());
	}


	@Override
	public DbContext getDbContext() {
		
		return this.getDbContext();
	}


	



	
	

}
