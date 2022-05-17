package spms.db.code.operation;




import spms.db.query.Query;

public class DeleteByIdsDbOperation<T> extends DeleteDbOperation<T>{


	public DeleteByIdsDbOperation(Query<T> query,final Object[] ids) {
		super(query);
		this.query.where().idIn(ids);
	}
	
	

}
