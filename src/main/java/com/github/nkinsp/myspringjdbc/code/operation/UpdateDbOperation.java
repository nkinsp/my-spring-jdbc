package com.github.nkinsp.myspringjdbc.code.operation;

import java.util.ArrayList;
import java.util.List;

import com.github.nkinsp.myspringjdbc.query.Query;
import com.github.nkinsp.myspringjdbc.table.ColumnFieldType;



public class UpdateDbOperation<T> extends AbstractDbOperation<T>{

	public UpdateDbOperation( Query<T> query) {
		super(query);
	}

	

	@Override
	public Object dbAdapter() {
		
		
		
		List<Object> params = new ArrayList<Object>();
		
		query.getColumnFields().forEach(field->{
			
			if(field.getType().equals(ColumnFieldType.VALUE)) {
				params.add(field.getValue());
			}else {
				params.addAll(field.getParams());
			}
			
		});
		
		params.addAll(query.getParams());
		String sql = query.getSqlBuilder().buildUpdateSQL();
		if(log.isInfoEnabled() && context.isShowSqlInfo()) {
			log.info("==> execute [sql={},params={}]",sql,params);
		}
		return context.update(sql,params.toArray());
	}



	

	

	
}
