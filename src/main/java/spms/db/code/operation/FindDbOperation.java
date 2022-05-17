package spms.db.code.operation;


import java.util.List;

import org.springframework.util.CollectionUtils;

import spms.db.query.Query;



public class FindDbOperation<T,En> extends FindBeanListDbOperation<T,En>{


	public FindDbOperation(Query<T> query, Class<En> enClass) {
		super(query, enClass);
	}

	
	@Override
	public Object dbAdapter() {
		List<En> list = findList();
		if(!CollectionUtils.isEmpty(list) ) {
			return list.get(0);
		}
		return null;
	}


	
	

	


	

}
