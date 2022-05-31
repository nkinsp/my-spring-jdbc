package com.github.nkinsp.myspringjdbc.query.conditions;

import java.lang.annotation.Annotation;
import java.util.function.Consumer;

import com.github.nkinsp.myspringjdbc.annotation.query.Like;
import com.github.nkinsp.myspringjdbc.query.ConditionAdapter;
import com.github.nkinsp.myspringjdbc.query.ConditionField;
import com.github.nkinsp.myspringjdbc.query.Query;
import com.github.nkinsp.myspringjdbc.util.StringUtils;

public class LikeConditionAdapter implements ConditionAdapter<Like>{

	@Override
	public boolean match(Class<? extends Annotation> annotationClass) {
		// TODO Auto-generated method stub
		return annotationClass.equals(Like.class);
	}

	@Override
	public <T> Consumer<Query<T>> adapter(ConditionField field) {
		
		
		String annotationValue = field.getAnnotatioValue("value");
		Boolean  prefix = field.getAnnotatioValue("prefix");
		Boolean  suffix = field.getAnnotatioValue("suffix");
		
		String colName = StringUtils.isEmpty(annotationValue)?StringUtils.humpToLine(field.getFieldName()):annotationValue;	
		return query->{
			
			query.like(colName,( prefix?"%":"")+field.getValue().toString()+(suffix?"%":""));
			
		};
	}

	
	

	


}
