package spms.db.code.operation;

import spms.db.query.Query;

public class PagingDbOperation<T, En> extends FindBeanListDbOperation<T, En> {

	private int pageNo;

	private int pageSize;

	public PagingDbOperation(Query<T> query, Class<En> enClass, int pageNo, int pageSize) {
		super(query, enClass);
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	@Override
	protected String builderQuerySql() {
		return query.getSqlBuilder().buildPagingSQL(pageNo, pageSize);
	}

}
