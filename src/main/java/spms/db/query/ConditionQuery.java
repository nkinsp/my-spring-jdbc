package spms.db.query;


public interface ConditionQuery {

	

	<T> void then(Query<T> query);
	
}
