package com.github.nkinsp.myspringjdbc.code.operation;

import java.util.List;

import com.github.nkinsp.myspringjdbc.query.Query;
import com.github.nkinsp.myspringjdbc.util.EntityUtils;

public class InsertEntityBatchDbOperation<T> extends InsertMapBatchDbOperation<T>{

	public InsertEntityBatchDbOperation(Query<T> query, List<T> data) {
		super(query, EntityUtils.entityListToMapList(data));

	}

}
