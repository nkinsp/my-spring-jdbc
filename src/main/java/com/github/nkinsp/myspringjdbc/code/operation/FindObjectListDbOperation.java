package com.github.nkinsp.myspringjdbc.code.operation;



import com.github.nkinsp.myspringjdbc.query.Query;



public class FindObjectListDbOperation<T,R> extends AbstractDbOperation<T>{

	private Class<R> resultType;
	
	public FindObjectListDbOperation(Query<T> query,Class<R> resultType) {
		super(query);
		this.resultType = resultType;
	}

	

	@Override
	public Object dbAdapter() {
		String sql = query.getSqlBuilder().buildSelectSQL();
		if(log.isInfoEnabled() && context.isShowSqlInfo()) {
			log.info("==> execute [sql={},params={}]",sql,query.getParams());
		}
		return context.queryForList(sql,resultType,query.getParams().toArray());
	}

	

	
}
