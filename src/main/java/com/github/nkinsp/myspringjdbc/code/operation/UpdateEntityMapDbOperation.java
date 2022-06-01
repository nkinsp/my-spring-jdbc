package com.github.nkinsp.myspringjdbc.code.operation;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.github.nkinsp.myspringjdbc.cache.CacheManager;
import com.github.nkinsp.myspringjdbc.query.Query;



public class UpdateEntityMapDbOperation<T> extends UpdateDbOperation<T> {

	
	private Object id;
	
	
	private Map<String, Object> getModelMap(Map<String, Object> map){
		
		Map<String, Object> model = new HashMap<String, Object>();
		
		for (Entry<String, Object> en : map.entrySet()) {
			
			model.put(tableMapping.getColumnName(en.getKey()), en.getValue());
		}
		
		return model;
		
		
	}
	
	public UpdateEntityMapDbOperation(Query<T> query,Map<String, Object> entityMap) {
		super(query);
		
		Map<String, Object> modelMap = getModelMap(entityMap);
		
		
		
		String idname = tableMapping.getPrimaryKey();
		if(!modelMap.containsKey(idname)) {
			throw new RuntimeException("No ID found ");
		}
		//主键
		 this.id = modelMap.get(idname);
		

		modelMap.forEach((field,value)->{
			if(!idname.equals(field)) {
				this.query.set(field, value);
			}
		});
		
		this.query.where().idEq(id);
		
	}
	
	@Override
	public Object dbAdapter() {
		// TODO Auto-generated method stub
		if(tableMapping.isCache()) {
			
			CacheManager manager = context.getCacheManager();
			
			if(manager != null) {
				Object key = manager.getCacheKeyGenerated().generated(tableMapping.getTableClass(), this.id);
				manager.delete(key);
			}			
		}
		return super.dbAdapter();
	}

}
