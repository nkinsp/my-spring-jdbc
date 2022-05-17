package spms.db.code.operation;


import spms.db.query.Query;
import spms.db.util.EntityUtils;

public class UpdateEntityDbOperation<T> extends UpdateEntityMapDbOperation<T>{

	public UpdateEntityDbOperation(Query<T> query, T model) {
		super(query, EntityUtils.entityToMap(model));
		// TODO Auto-generated constructor stub
	}

}
