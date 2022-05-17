package spms.db.code.operation;

import java.util.List;

import spms.db.query.Query;
import spms.db.util.EntityUtils;

public class UpdateEntityBatchDbOperation<T> extends UpdateEntityMapBatchDbOperation<T>{

	public UpdateEntityBatchDbOperation(Query<T> query, List<T> data) {
		super(query, EntityUtils.entityListToMapList(data));
		// TODO Auto-generated constructor stub
	}

}
