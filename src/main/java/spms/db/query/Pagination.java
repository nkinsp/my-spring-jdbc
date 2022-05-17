package spms.db.query;

public interface Pagination extends ConditionQuery {

	
	Integer getPageNo();
	
	Integer getPageSize();
}
