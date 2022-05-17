package com.github.nkinsp.myspringjdbc.query.impl;

import com.github.nkinsp.myspringjdbc.code.DbContext;
import com.github.nkinsp.myspringjdbc.query.Query;
import com.github.nkinsp.myspringjdbc.table.TableMapping;

public class SQLServerDialectQueryImpl<T> extends AbstractSupportQueryImpl<T>{

	
	public SQLServerDialectQueryImpl(TableMapping<T> tableMapping, DbContext context) {
		super(tableMapping, context);
	}

	@Override
	public Query<T> limit(int pageNo, int pageSize) {
		return condition("OFFSET ? ROWS FETCH NEXT ? ONLY ").addParams(pageNo,pageSize);
	}

}
