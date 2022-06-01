package com.github.nkinsp.myspringjdbc.code.operation;

import java.util.List;

import com.github.nkinsp.myspringjdbc.query.Query;
import com.github.nkinsp.myspringjdbc.util.EntityUtils;

public class UpdateEntityBatchDbOperation<T> extends UpdateEntityMapBatchDbOperation<T>{

	public UpdateEntityBatchDbOperation(Query<T> query, List<T> data) {
		super(query, EntityUtils.entityListToMapList(data));
	}

}
