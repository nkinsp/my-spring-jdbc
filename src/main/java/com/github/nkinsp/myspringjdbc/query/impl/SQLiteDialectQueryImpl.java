package com.github.nkinsp.myspringjdbc.query.impl;

import com.github.nkinsp.myspringjdbc.code.DbContext;
import com.github.nkinsp.myspringjdbc.query.Query;
import com.github.nkinsp.myspringjdbc.table.TableMapping;

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
