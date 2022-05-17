package com.github.nkinsp.myspringjdbc.code.operation;

import java.util.LinkedHashMap;
import java.util.Map;

import com.github.nkinsp.myspringjdbc.query.Query;
import com.github.nkinsp.myspringjdbc.table.TableMapping;



/**
 * 逻辑删除
 * @author yue
 *
 * @param <T>
 */
public class LogicDeleteByIdDbOperation<T> extends UpdateEntityMapDbOperation<T>{

	
	private static <T> Map<String, Object> getEntityMap(TableMapping<T> mapping,Object id){
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put(mapping.getPrimaryKey(), id);
		map.put(mapping.getLogicDeleteName(), 1);
		return map;
	}
	
	public LogicDeleteByIdDbOperation(Query<T> query, Object id) {
		
		super(query, getEntityMap(query.getTableMapping(), id));
		
		
	}

	
	

}
