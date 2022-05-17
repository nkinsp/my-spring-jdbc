package spms.db.code.operation;

import java.util.List;

import spms.db.query.Query;
import spms.db.util.EntityUtils;

public class InsertEntityBatchDbOperation<T> extends InsertMapBatchDbOperation<T>{

	public InsertEntityBatchDbOperation(Query<T> query, List<T> data) {
		super(query, EntityUtils.entityListToMapList(data));

	}

}
