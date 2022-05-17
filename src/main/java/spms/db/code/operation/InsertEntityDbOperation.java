package spms.db.code.operation;



import spms.db.query.Query;
import spms.db.util.EntityUtils;


public class InsertEntityDbOperation<T> extends InsertMapDbOperation<T> {

	public InsertEntityDbOperation(Query<T> query, T model) {

		super(query, EntityUtils.entityToMap(model));

	}

}
