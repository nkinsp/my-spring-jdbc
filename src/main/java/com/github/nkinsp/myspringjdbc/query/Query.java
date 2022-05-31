package com.github.nkinsp.myspringjdbc.query;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.nkinsp.myspringjdbc.code.DbContext;
import com.github.nkinsp.myspringjdbc.code.DbOperation;
import com.github.nkinsp.myspringjdbc.table.ColumnField;
import com.github.nkinsp.myspringjdbc.table.TableMapping;
import com.github.nkinsp.myspringjdbc.util.ObjectUtils;

public interface Query<T> extends CrudQuery<T>{

	
	/**
	 * 查询的列名
	 * @param cols
	 * @return
	 */
	Query<T> columns(String...cols);
	
	
	/**
	 * 获取参数
	 * @return
	 */
	List<Object> getParams();
	
	
	List<ColumnField> getColumnFields();
	
	/**
	 * 添加参数
	 * @param params
	 * @return
	 */
	Query<T> addParams(Object...params);
	
	Query<T> where();
	
	/**
	 * 添加查询条件
	 * @param conditions
	 * @return
	 */
	Query<T> condition(String...conditions);
	
	
	default Query<T> addCondition(String condition,Object...params){
		
		return condition(condition).addParams(params);
	}
	
	
	Query<T> addFilter(String filter);
	
	/**
	 * WHERE 
	 * @param where
	 * @param params
	 * @return
	 */
	default Query<T> where(String conditions,Object...params){
		
		return condition(conditions).addParams(params);
	}
	
	
	/**
	 * 更新的字段
	 * @param field
	 * @param value
	 * @return
	 */
	Query<T> set(String field,Object value);
	
	/**
	 * 更新的字段
	 * @param map
	 * @return
	 */
	default Query<T> setMap(Map<String, Object> map){
		for (Entry<String, Object> en : map.entrySet()) {
			set(en.getKey(), en.getValue());
		}
		return this;
	}
	
	
	/**
	 * 更新 :field = (:sql) 
	 * @param field
	 * @param value
	 * @param values
	 * @return
	 */
	Query<T> setSql(String field,String value,Object...values);
	
	/**
	 * 主键 = ?
	 * @param value
	 * @return
	 */
	default Query<T> idEq(Object value){
		return field(getTableMapping().getPrimaryKey()).eq(value);
	}
	
	
	/**
	 * =?
	 * @param value
	 * @return
	 */
	default Query<T> eq(Object value){
		return condition("=","?").addParams(value);
	}
	
	
	/**
	 * field = ?
	 * @param field
	 * @param value
	 * @return
	 */
	default Query<T> eq(String field,Object value){
		return field(field).eq(value);
	}
	

	/**
	 * prefix.name
	 * @param prefix
	 * @param name
	 * @return
	 */
	default Query<T> field(String prefix,String name){
		String columnName = getTableMapping().getColumnName(name);
		return columns(prefix+"."+columnName);
	}
	
	/**
	 * 字段
	 * @param name
	 * @return
	 */
	default Query<T> field(String name){
		String columnName = getTableMapping().getColumnName(name);
		return condition(columnName);
	}
	
	
	/**
	 * 主键 in (...)
	 * @param values
	 * @return
	 */
	default Query<T> idIn(Object...values){
		return field(getTableMapping().getPrimaryKey()).in(values);
	}
	
	/**
	 * in(...)
	 * @param values
	 * @return
	 */
	default Query<T> in(Object...values){
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		for (int i = 0; i < values.length; i++) {
			if(i > 0) {
				builder.append(",");
			}
			builder.append("?");
		}		
		builder.append(")");
		return condition("IN").condition(builder.toString()).addParams(values);
	}
	
	/**
	 * 字段 in (...)
	 * @param field
	 * @param values
	 * @return
	 */
	default Query<T> in(String field,Object...values){
		return field(field).in(values);
	}
	
	
	/**
	 * order by 
	 * @param fields
	 * @return
	 */
	default Query<T> orderBy(String orderBy){
	
		 return addFilter("ORDER BY").addFilter(orderBy);
		
		
	}
	
	default Query<T> and(){
		
		return condition("AND");
	}
	
	/**
	 * order by :fields desc
	 * @param fields
	 * @return
	 */
	default Query<T> orderByDesc(String filter){
		return orderBy(filter).addFilter("DESC");
	}
	
	/**
	 * order by :fileds ASC
	 * @param fields
	 * @return
	 */
	default Query<T> orderByAsc(String orderBy){
		return orderBy(orderBy).addFilter("ASC");
	}
	
	default Query<T> andThen(boolean eq,Consumer<Query<T>> query){
		if(eq) {
			and();
			query.accept(this);
		}
		return this;
	}
	
	
	
	
	default <V> Query<T> andNotEmptyThen(V v,Consumer<Query<T>> query){
		
		if(ObjectUtils.isEmpty(v)) {
			return this;
		}
		and();
		query.accept(this);

		return this;
	}
	
	default <V> Query<T> andNotEmptyThen(V v,BiConsumer<Query<T>,V> query){
		
		if(ObjectUtils.isEmpty(v)) {
			return this;
		}
		and();
		query.accept(this,v);

		return this;
	}
	
	default <V> Query<T> notEmptyThen(V v,Consumer<Query<T>> consumer){
		
		if(ObjectUtils.isEmpty(v)) {
			return this;
		}
		
		consumer.accept(this);

		return this;
	}
	
	
	
	/**
	 * 
	 * like ?
	 * @param field
	 * @param value
	 * @return
	 */
	default Query<T> like(String field,String value){
		return condition(field).condition("LIKE ?").addParams(value);
	}
	
	/**
	 * !=
	 * @return
	 */
	default Query<T> ne(){
		return condition("!=");
	}
	
	/**
	 * field != ?
	 * @param field
	 * @param value
	 * @return
	 */
	default Query<T> ne(String field,Object value){
		
		return field(field).ne().addParams(value);
	}
	
	/**
	 * >
	 * @return
	 */
	default Query<T> gt(){
		return condition(">");
	}
	
	/**
	 * field > ?
	 * @param field
	 * @param value
	 * @return
	 */
	default Query<T> gt(String field,Object value){
		
		return field(field).gt().condition("?").addParams(value);
	}
	
	/**
	 * >=
	 * @return
	 */
	default Query<T> ge(){
		return condition(">=");
	}
	
	/**
	 * field >= ?
	 * @param field
	 * @param value
	 * @return
	 */
	default Query<T> ge(String field,Object value){
		
		return field(field).ge().condition("?").addParams(value);
	}
	
	/**
	 * <
	 * @return
	 */
	default Query<T> lt(){
		return condition("<");
	}
	
	/**
	 * field > ?
	 * @param field
	 * @param value
	 * @return
	 */
	default Query<T> lt(String field,Object value){
		
		return field(field).lt().condition("?").addParams(value);
	}
	
	/**
	 * <=
	 * @return
	 */
	default Query<T> le(){
		return condition("<=");
	}
	
	/**
	 * field <= ?
	 * @param field
	 * @param value
	 * @return
	 */
	default Query<T> le(String field,Object value){
		
		return field(field).le().condition("?").addParams(value);
	}
	
	/**
	 * BETWEEN ? AND ?
	 * @param field
	 * @param start
	 * @param end
	 * @return
	 */
	default Query<T> between(String field,Object start,Object end){
		return condition(field).condition("BETWEEN ? AND ?").addParams(start,end);
	}
	
	/**
	 * 
	 * @param tableClass
	 * @return
	 */
	Query<T> join(Class<?>...tableClass);
	
	/**
	 * 
	 * @param tables
	 * @return
	 */
	default  Query<T> join(String...tables){
		for (String table : tables) {
			condition(",",table);
		}
		return this;
	}
	
	/**
	 * COUNT()
	 * @param str
	 * @return
	 */
	default Query<T> count(String str) {
		
		return columns("COUNT("+str+")");
	}
	
	/**
	 * 指定返回条数
	 * @param start
	 * @param offset
	 * @return
	 */
	default Query<T> limit(int start,int offset){
		
		return addFilter("LIMIT ? , ?").addParams(start,offset);
	}
	
	default Query<T> groupBy(String groupBy){
		
		return addFilter("GROUP BY").addFilter(groupBy);
	}
	
	
	default <R> R execute(DbOperation adapter) {
		
		try {
			return adapter.execute();
		} finally {
			adapter = null;
		}
		
	}
	
	
	TableMapping<T> getTableMapping();
	
	QuerySqlBuilder<T> getSqlBuilder();

	DbContext getDbContext();
	
	
}
