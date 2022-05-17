package spms.db.query.impl;

import spms.db.code.DbContext;
import spms.db.table.TableMapping;

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
