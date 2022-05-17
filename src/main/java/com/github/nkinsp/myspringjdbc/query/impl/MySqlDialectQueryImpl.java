package com.github.nkinsp.myspringjdbc.query.impl;

import com.github.nkinsp.myspringjdbc.code.DbContext;
import com.github.nkinsp.myspringjdbc.table.TableMapping;

/**
 * mysql  数据库分页
 * @author hanjiang.Yue
 *
 * @param <T>
 */
public class MySqlDialectQueryImpl<T> extends AbstractSupportQueryImpl<T>{


	public MySqlDialectQueryImpl(TableMapping<T> tableMapping, DbContext context) {
		super(tableMapping, context);
	}

	
	




}
