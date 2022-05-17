package com.github.nkinsp.myspringjdbc.code.operation;



import com.github.nkinsp.myspringjdbc.query.Query;
import com.github.nkinsp.myspringjdbc.util.EntityUtils;


public class InsertEntityDbOperation<T> extends InsertMapDbOperation<T> {

	public InsertEntityDbOperation(Query<T> query, T model) {

		super(query, EntityUtils.entityToMap(model));

	}

}
