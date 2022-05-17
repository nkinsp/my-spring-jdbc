package spms.db.code.operation;

import spms.db.query.Query;

public class DeleteByIdDbOperation<T> extends DeleteDbOperation<T>{

	public DeleteByIdDbOperation(Query<T> query,Object id) {
		super(query);
		this.query.where().idEq(id);
		
	
		
	}

}
