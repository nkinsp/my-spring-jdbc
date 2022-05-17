package spms.db.code.operation;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import spms.db.query.Query;



public class UpdateEntityMapDbOperation<T> extends UpdateDbOperation<T> {

	
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
		Object id = modelMap.get(idname);

		modelMap.forEach((field,value)->{
			if(!idname.equals(field)) {
				this.query.set(field, value);
			}
		});
		
		this.query.where().idEq(id);
		
	}

}
