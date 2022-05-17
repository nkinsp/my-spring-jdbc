package com.github.nkinsp.myspringjdbc.code.operation;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.nkinsp.myspringjdbc.code.DbContext;
import com.github.nkinsp.myspringjdbc.code.DbOperation;
import com.github.nkinsp.myspringjdbc.query.Query;
import com.github.nkinsp.myspringjdbc.query.QuerySqlBuilder;
import com.github.nkinsp.myspringjdbc.table.TableMapping;


/**
 * @author hanjiang.Yue
 * @param <T>
 */
public abstract class AbstractDbOperation<T> implements DbOperation{

	public  DbContext context;
	
	public  Query<T> query;
	
	
	public  TableMapping<T> tableMapping;
	
	public static Logger log = LoggerFactory.getLogger(DbOperation.class);
	
	
	
	/**
	 * 执行数据库操作
	 * @author hanjiang.Yue
	 * @param query
	 * @return
	 */
	public  abstract Object dbAdapter();
	
	

	@SuppressWarnings("unchecked")
	@Override
	public <R> R execute() {
		try {
			return (R) dbAdapter();
		} finally {
			//释放query
			 QuerySqlBuilder<T> queryBuilder = (QuerySqlBuilder<T>) query;
			 queryBuilder.release();
			 query = null;
		}
	}

	public AbstractDbOperation( Query<T> query) {
		super();
		this.query = query;
		this.context = query.getDbContext();
		this.tableMapping = this.query.getTableMapping();
	}
	
	


	
	
	
	
}
