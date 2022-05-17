package spms.db.query.impl;

import spms.db.code.DbContext;
import spms.db.table.TableMapping;

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
