package spms.db.query.impl;

import spms.db.code.DbContext;
import spms.db.query.Query;
import spms.db.table.TableMapping;

/**
 * SQLite 数据库分页
 * @author hanjiang.Yue
 * @param <T>
 */
public class SQLiteDialectQueryImpl<T> extends AbstractSupportQueryImpl<T>{


	

	public SQLiteDialectQueryImpl(TableMapping<T> tableMapping, DbContext context) {
		super(tableMapping, context);
	}



	@Override
	public Query<T> limit(int pageNo, int pageSize) {
		return condition("LIMIT ? OFFSET ? ").addParams(pageSize,(pageNo-1)*pageSize);
	}
	

}
