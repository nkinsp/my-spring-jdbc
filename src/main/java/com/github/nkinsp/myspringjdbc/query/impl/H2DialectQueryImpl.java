package com.github.nkinsp.myspringjdbc.query.impl;

import com.github.nkinsp.myspringjdbc.code.DbContext;
import com.github.nkinsp.myspringjdbc.table.TableMapping;

/**
 * h2database 
 * @author  hanjiang.Yue
 *
 * @param <T>
 */
public class H2DialectQueryImpl<T> extends AbstractSupportQueryImpl<T>{

	public H2DialectQueryImpl(TableMapping<T> tableMapping, DbContext context) {
		super(tableMapping, context);
	}



}
