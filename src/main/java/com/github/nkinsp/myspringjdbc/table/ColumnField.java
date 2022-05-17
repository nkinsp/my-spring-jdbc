package com.github.nkinsp.myspringjdbc.table;

import java.util.ArrayList;
import java.util.List;

public class ColumnField {

	private String name;
	
	private Object value;
	
	private ColumnFieldType type;
	
	private List<Object> params;

	public ColumnField(String name, Object value, ColumnFieldType type) {
		super();
		this.name = name;
		this.value = value;
		this.type = type;
		if (type == ColumnFieldType.SQL) {
			this.setParams(new ArrayList<Object>());
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public ColumnFieldType getType() {
		return type;
	}

	public void setType(ColumnFieldType type) {
		this.type = type;
	}

	public List<Object> getParams() {
		return params;
	}

	public void setParams(List<Object> params) {
		this.params = params;
	}
	
	
	
}
