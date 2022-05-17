package com.github.nkinsp.myspringjdbc.query.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.github.nkinsp.myspringjdbc.code.DbContext;
import com.github.nkinsp.myspringjdbc.code.operation.DeleteDbOperation;
import com.github.nkinsp.myspringjdbc.code.operation.FindBeanListDbOperation;
import com.github.nkinsp.myspringjdbc.code.operation.FindDbOperation;
import com.github.nkinsp.myspringjdbc.code.operation.FindObjectDbOperation;
import com.github.nkinsp.myspringjdbc.code.operation.PagingDbOperation;
import com.github.nkinsp.myspringjdbc.code.operation.UpdateDbOperation;
import com.github.nkinsp.myspringjdbc.query.Query;
import com.github.nkinsp.myspringjdbc.query.QuerySqlBuilder;
import com.github.nkinsp.myspringjdbc.table.ColumnField;
import com.github.nkinsp.myspringjdbc.table.ColumnFieldType;
import com.github.nkinsp.myspringjdbc.table.TableMapping;

public  class AbstractSupportQueryImpl<T> implements Query<T>,QuerySqlBuilder<T>{
	
	
	protected boolean where = false;
	
	private List<String> conditions;
	
	private List<String> filters;
	
	private List<String> columns;
	
	private List<ColumnField> fields;
	
	private DbContext dbContext;
	
	private TableMapping<T> tableMapping;
	
	private List<Object> params;
	

	
	private String tableAlias;
	

	public AbstractSupportQueryImpl( TableMapping<T> tableMapping,DbContext dbContext) {
		super();
		this.dbContext = dbContext;
		this.tableMapping = tableMapping;
		this.columns = new ArrayList<>();
		this.conditions = new ArrayList<>();
		this.fields = new ArrayList<>();
		this.params = new ArrayList<>();
		this.filters = new ArrayList<String>();
	}
	

	@Override
	public String buildDeleteSQL() {
		if (this.conditions.isEmpty()) {
			throw new RuntimeException("没有删除条件");
		}

		return new StringBuilder().append("DELETE ").append("FROM ").append(tableMapping.getTableName()).append(" ")
				.append("WHERE ").append(getConditions()).toString();
	}

	@Override
	public String buildInsertSQL(Map<String, Object> map) {
		
		
		
		StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO ");
		builder.append(tableMapping.getTableName());
		builder.append(" (");
		builder.append(map.keySet().stream().collect(Collectors.joining(",")));
		builder.append(") ");
		builder.append("VALUES ");
		builder.append("(");
		builder.append(map.values().stream().map(String::valueOf).collect(Collectors.joining(",")));
		builder.append(")");
		
		return builder.toString();
		
		
	}

	@Override
	public void release() {
		// TODO Auto-generated method stub
		this.columns = null;
		this.conditions = null;
		this.fields = null;
		this.params = null;
		this.filters = null;
		
	}

	
	@Override
	public Query<T> addFilter(String filter) {
		this.filters.add(filter);
		return this;
	}
	

	@Override
	public Query<T> columns(String... cols) {
		// TODO Auto-generated method stub
		for (String col : cols) {
			this.columns.add(col);
		}
		return this;
	}

	@Override
	public List<Object> getParams() {
		// TODO Auto-generated method stub
		return this.params;
	}

	@Override
	public Query<T> addParams(Object... params) {
		// TODO Auto-generated method stub
		for (Object object : params) {
			this.params.add(object);
		}
		return this;
	}

	@Override
	public Query<T> where() {
		this.where = true;
		return this;
	}

	@Override
	public Query<T> condition(String... conditions) {
		// TODO Auto-generated method stub
		for (String str : conditions) {
			this.conditions.add(str);
		}
		return this;
	}

	@Override
	public Query<T> set(String field, Object value) {
		
	   this.fields.add(new ColumnField(tableMapping.getColumnName(field), value, ColumnFieldType.VALUE));
		
		return this;
	}

	@Override
	public Query<T> setSql(String field, String value, Object... values) {
		
		ColumnField columnField = new ColumnField(tableMapping.getColumnName(field), value, ColumnFieldType.VALUE);
		
		columnField.getParams().addAll(Arrays.asList(values));
		
		this.fields.add(columnField);
		
		return this;
	}

	

	@Override
	public Query<T> join(Class<?>... tableClass) {
		
		String[] tables = new String[tableClass.length];
		for (int i = 0; i < tables.length; i++) {
			tables[i] = dbContext.findTableMapping(tableClass[i]).getTableName();
		}		
		return join(tables);
	}

	

	

	@Override
	public TableMapping<T> getTableMapping() {
		
		return tableMapping;
	}

	@Override
	public QuerySqlBuilder<T> getSqlBuilder() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public String buildPagingSQL(int pageNo, int pageSize) {
//		limit((pageNo - 1) * pageSize, pageSize);
		
		StringBuilder sql = new StringBuilder(buildSelectSQL())
				.append(" LIMIT ?,?");
		addParams((pageNo - 1) * pageSize,pageSize);
		
		return sql.toString();
	}

	@Override
	public String buildSelectSQL() {
		
		return buildSelectSQL(this.columns);
	}

	@Override
	public String buildSelectSQL(List<String> cols) {
		StringBuilder sql = new StringBuilder().append("SELECT ");
		if (cols.isEmpty()) {
			sql.append("*");
		}
		for (int i = 0; i < cols.size(); i++) {
			if (i > 0)
				sql.append(",");
			sql.append(cols.get(i));
		}
		sql.append(" FROM ");
		sql.append(tableMapping.getTableName());
		if(!StringUtils.isEmpty(this.tableAlias)) {
			sql.append(" AS ");
			sql.append(this.tableAlias);
		}
		sql.append(" ");
		//是否有逻辑删除
		if(tableMapping.isLogicDelete()) {
			
			if(!this.conditions.isEmpty()) {
				this.and();
			}
			this.eq(tableMapping.getLogicDeleteName(), 0);
		}
		if(!this.conditions.isEmpty()) {
			sql.append("WHERE ");	
		}
		sql.append(getConditions());
		if(!this.filters.isEmpty()) {
			sql.append(" ");
			sql.append(getFilters());
		}
		return sql.toString();
	}
	
	@Override
	public String buildUpdateSQL(Map<String, Object> updateMap) {
		if(this.conditions.isEmpty()) {
			throw new RuntimeException("没有更新条件");
		}
		StringBuilder builder = new StringBuilder();
		builder.append("UPDATE ");
		builder.append(tableMapping.getTableName());
		builder.append(" SET ");
		int i = 0;
		for (Entry<String, Object> en : updateMap.entrySet()) {
			if (i > 0) {
				builder.append(" , ");
			}
			builder.append(en.getKey());
			builder.append(" = ");
			builder.append(en.getValue());
			i++;
		}
		builder.append(" WHERE ");
		builder.append(getConditions());
		return builder.toString();
		
	}
	

	@Override
	public String buildUpdateSQL() {
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		for (ColumnField field : this.fields) {
			map.put(tableMapping.getColumnName(field.getName()), field.getType().equals(ColumnFieldType.VALUE)?"?":field.getValue());
		}
		return buildUpdateSQL(map);
	}

	private String getConditions() {
		return this.conditions.stream().collect(Collectors.joining(" "));
	}
	
	private String getFilters() {
		return this.filters.stream().collect(Collectors.joining(" "));
	}

	@Override
	public List<ColumnField> getColumnFields() {
		// TODO Auto-generated method stub
		return this.fields;
	}

	@Override
	public DbContext getDbContext() {
		// TODO Auto-generated method stub
		return this.dbContext;
	}

	@Override
	public T find() {
		
		return find(tableMapping.getTableClass());
	}

	@Override
	public <R> R findUnique(Class<R> resultClass) {
	
		return execute(new FindObjectDbOperation<T, R>(this, resultClass));
	}

	@Override
	public <En> En find(Class<En> enClass) {
		// TODO Auto-generated method stub
		return execute(new FindDbOperation<>(this,enClass));
	}

	@Override
	public List<T> findList() {
		
		return findList(tableMapping.getTableClass());
	}

	@Override
	public <En> List<En> findList(Class<En> enClass) {
	
		return execute(new FindBeanListDbOperation<T, En>(this, enClass));
	}

	@Override
	public List<T> findList(int pageNo, int pageSize) {
		
		return findList(pageNo, pageSize, tableMapping.getTableClass());
	}

	@Override
	public <En> List<En> findList(int pageNo, int pageSize, Class<En> enClass) {
	
		
		return execute(new PagingDbOperation<>(this,enClass, pageNo, pageSize));
	}

	@Override
	public int delete() {
		
		return execute(new DeleteDbOperation<T>(this));
	}

	@Override
	public int update() {
		
		return execute(new UpdateDbOperation<T>(this));
	}

	

	

}
