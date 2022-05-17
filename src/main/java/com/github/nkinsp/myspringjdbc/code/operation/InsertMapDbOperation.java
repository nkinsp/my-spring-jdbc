package com.github.nkinsp.myspringjdbc.code.operation;


import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.github.nkinsp.myspringjdbc.query.Query;
import com.github.nkinsp.myspringjdbc.table.ColumnField;
import com.github.nkinsp.myspringjdbc.table.ColumnFieldType;
import com.github.nkinsp.myspringjdbc.table.IdType;
import com.github.nkinsp.myspringjdbc.util.CastUtils;


public class InsertMapDbOperation<T> extends AbstractDbOperation<T>{

	private Map<String, Object> model;
	public InsertMapDbOperation(Query<T> query,Map<String, Object> model) {
		super(query);
		this.model = model;
	}

	protected List<ColumnField> getColumnFields(Map<String, Object> model){
	
		List<ColumnField> fields = new ArrayList<ColumnField>();
		//不存在
		if(!model.containsKey(tableMapping.getPrimaryKey())) {
			ColumnField field = context.getPrimaryKeyGenerated().generated(tableMapping);
			if(field != null) {
				fields.add(field);
			}
			
		}
		for (Entry<String, Object> en : model.entrySet()) {
			
			fields.add(new ColumnField(tableMapping.getColumnName(en.getKey()), en.getValue(), ColumnFieldType.VALUE));
		}
		
		return fields;
	}
	
	protected Map<String, Object> getModelMap(List<ColumnField> fields){
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		for (ColumnField field : fields) {
			map.put(field.getName(), field.getType().equals(ColumnFieldType.VALUE)?"?":field.getValue());
			
		}
		return map;
		
	}
	
	private Object insertGetIdValue(String sql,Object[] params) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		this.context.update((conn) -> {
			PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			for (int j = 0; j < params.length; j++) {
				statement.setObject((j + 1), params[j]);
			}
			return statement;
		}, keyHolder);

		Number key = keyHolder.getKey();
		return CastUtils.cast(key, tableMapping.getIdClassType());
	}

	@Override
	public Object dbAdapter() {
		
		
		List<ColumnField> columnFields = getColumnFields(this.model);
		
		Map<String, Object> modelMap =  getModelMap(columnFields);
		

		Object[] params = columnFields.stream().map(ColumnField::getValue).toArray();
		
		String primaryKey = tableMapping.getPrimaryKey();
		
		String sql = query.getSqlBuilder().buildInsertSQL(modelMap);
		if(log.isInfoEnabled() && context.isShowSqlInfo()) {
			log.info("==> execute [sql={},params={}]",sql,params);
		}
		//自增id 
		if(tableMapping.getIdType().equals(IdType.AUTO)) {
			return insertGetIdValue(sql, params);
		}
		
		context.update(sql, params);
	
		return modelMap.get(primaryKey);
	}




	
	

	

}
