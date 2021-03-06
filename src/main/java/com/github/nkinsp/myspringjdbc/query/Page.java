package com.github.nkinsp.myspringjdbc.query;

import java.util.List;

public class Page<T> {

	private Integer count;
	
	private List<T> list;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public Page(Integer count, List<T> list) {
		super();
		this.count = count;
		this.list = list;
	}

	public Page() {
		super();
		
	} 
	
	
}
