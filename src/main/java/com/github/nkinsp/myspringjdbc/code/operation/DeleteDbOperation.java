package com.github.nkinsp.myspringjdbc.code.operation;


import com.github.nkinsp.myspringjdbc.query.Query;

public class DeleteDbOperation<T> extends AbstractDbOperation<T>{

	public DeleteDbOperation(Query<T> query) {
		super(query);
	}

	
	

	

	@Override
	public Object dbAdapter() {
		String sql = query.getSqlBuilder().buildDeleteSQL();
		if(log.isInfoEnabled() && context.isShowSqlInfo()) {
			log.info("==> execute [sql={},params={}]",sql,query.getParams());
		}
		return context.update(sql, query.getParams().toArray());
	}

	









	

}
