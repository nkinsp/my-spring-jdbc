package spms.db.query;

import java.util.List;
import java.util.Map;


public interface QuerySqlBuilder<T> {

	
	String buildSelectSQL();
	
	String buildSelectSQL(List<String> columns);
	
	String buildDeleteSQL();
	
	/**
	 * 构建新增INSERT sql
	 * @author hanjiang.Yue
	 * @param fields
	 * @return
	 */
	String buildInsertSQL(Map<String, Object> map);	

	String buildUpdateSQL();
	
	String buildUpdateSQL(Map<String, Object> updateMap);

	String buildPagingSQL(int pageNo, int pageSize);

	
	void release();
}
