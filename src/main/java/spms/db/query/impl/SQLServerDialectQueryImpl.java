package spms.db.query.impl;

import spms.db.code.DbContext;
import spms.db.query.Query;
import spms.db.table.TableMapping;

public class SQLServerDialectQueryImpl<T> extends AbstractSupportQueryImpl<T>{

	
	public SQLServerDialectQueryImpl(TableMapping<T> tableMapping, DbContext context) {
		super(tableMapping, context);
	}

	@Override
	public Query<T> limit(int pageNo, int pageSize) {
		return condition("OFFSET ? ROWS FETCH NEXT ? ONLY ").addParams(pageNo,pageSize);
	}

}
