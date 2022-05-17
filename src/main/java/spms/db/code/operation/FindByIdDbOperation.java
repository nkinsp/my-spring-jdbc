package spms.db.code.operation;

import spms.db.query.Query;

public class FindByIdDbOperation<T,En> extends FindDbOperation<T, En>{

	
	public FindByIdDbOperation(Query<T> query, Class<En> enClass,Object id) {
		super(query, enClass);
		this.query.where().idEq(id);
		
	}

}
