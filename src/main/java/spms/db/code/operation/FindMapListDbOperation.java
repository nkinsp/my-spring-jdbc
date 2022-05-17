package spms.db.code.operation;




import spms.db.query.Query;


public class FindMapListDbOperation<T> extends AbstractDbOperation<T>{

	public FindMapListDbOperation(Query<T> query) {
		super(query);
	}
	
	
	@Override
	public Object dbAdapter() {
		String sql = query.getSqlBuilder().buildSelectSQL();
		if(log.isInfoEnabled() && context.isShowSqlInfo()) {
			log.info("==> execute [sql={},params={}]",sql,query.getParams());
		}
		return context.queryForList(sql, query.getParams().toArray());
	}



	

}
