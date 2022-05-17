package spms.db.code.operation;


import spms.db.query.Query;

public class FindListByIdsDbOperation<T,En> extends FindBeanListDbOperation<T,Object> {

	
	
	@SuppressWarnings("unchecked")
	public FindListByIdsDbOperation(Query<T> query,Object[] ids) {
		super(query,(Class<Object>) query.getTableMapping().getTableClass());
		this.query.where().idIn(ids);
		
	}
	
	

	

	


}
