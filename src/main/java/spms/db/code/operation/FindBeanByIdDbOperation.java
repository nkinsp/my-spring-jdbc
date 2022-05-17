package spms.db.code.operation;

import spms.db.query.Query;

public class FindBeanByIdDbOperation<T> extends FindByIdDbOperation<T, T>{

	
	public FindBeanByIdDbOperation(Query<T> query,Object id) {
		super(query, query.getTableMapping().getTableClass(),id);
		
	}

}
