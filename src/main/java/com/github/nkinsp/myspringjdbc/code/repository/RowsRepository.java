package com.github.nkinsp.myspringjdbc.code.repository;

import java.util.List;
import java.util.function.Consumer;

import com.github.nkinsp.myspringjdbc.code.Rows;
import com.github.nkinsp.myspringjdbc.query.ConditionQuery;
import com.github.nkinsp.myspringjdbc.query.Page;
import com.github.nkinsp.myspringjdbc.query.PaginationConditionQuery;
import com.github.nkinsp.myspringjdbc.query.Query;

public interface RowsRepository<T,Id> {

	
	
     <E> Rows<E> findRows(Class<E> entityClass,List<Id> ids);
     
    default  Rows<T> findRows(List<Id> ids){
    	return findRows(getTableClass(),ids);
    }
     
    <E> Rows<E> findRows(Class<E> entiryClass,Consumer<Query<T>> query);
    
     default Rows<T> findRows(Consumer<Query<T>> query){
    	 return findRows(getTableClass(), query);
     }
     
     <E> Rows<E> findRowsBy(Class<E> entityClass,ConditionQuery query);
     
     default  Rows<T> findRowsBy(ConditionQuery query){
    	 return findRowsBy(getTableClass(),query);
     }
     
     <E> Rows<E>  findRowsBy(Class<E> entityClass,PaginationConditionQuery query);
     
    default Rows<T> findRowsBy(PaginationConditionQuery query){
    	return findRowsBy( getTableClass(),query);
    }
    
     
     Class<T> getTableClass();
	
}
