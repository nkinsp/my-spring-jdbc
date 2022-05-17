package spms.db.code.operation;

import java.util.Map;

import spms.db.code.DbOperation;
import spms.db.query.Query;
import spms.db.table.TableMapping;
import spms.db.util.EntityUtils;

public class InsertOrUpdateDbOperation<T> implements DbOperation{

	
	private Query<T> query;
	
	private T model;
	
	public InsertOrUpdateDbOperation(Query<T> query, T model) {
		this.query = query;
		this.model = model;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public <R> R execute() {
		
		TableMapping<? extends Object> mapping = query.getDbContext().findTableMapping(model.getClass());
		Map<String, Object> entityMap = EntityUtils.entityToMap(model);
		
		//更新数据
		if(entityMap.containsKey(mapping.getPrimaryKey())) {
			 new UpdateEntityMapDbOperation<T>(query, entityMap).execute();
			 return (R) entityMap.get(mapping.getPrimaryKey());
		}
		//添加数据
		return new InsertMapDbOperation<T>(query, entityMap).execute();
		
	}

}
