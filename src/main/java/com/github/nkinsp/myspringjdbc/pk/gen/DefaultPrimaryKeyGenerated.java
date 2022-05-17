package com.github.nkinsp.myspringjdbc.pk.gen;

import java.util.UUID;

import com.github.nkinsp.myspringjdbc.pk.PrimaryKeyGenerated;
import com.github.nkinsp.myspringjdbc.table.ColumnField;
import com.github.nkinsp.myspringjdbc.table.ColumnFieldType;
import com.github.nkinsp.myspringjdbc.table.IdType;
import com.github.nkinsp.myspringjdbc.table.TableMapping;

/**
 * 生成主键类型
 * @author hanjiang.Yue
 *
 */
public class DefaultPrimaryKeyGenerated implements PrimaryKeyGenerated{
	
	@Override
	public ColumnField generated(TableMapping<?> mapping) {
		IdType idType = mapping.getIdType();
		if(idType == IdType.UUID) {
			return new ColumnField(mapping.getPrimaryKey(),UUID.randomUUID().toString().replace("-", ""),ColumnFieldType.VALUE);
		}
			
		return null;
	}

	public DefaultPrimaryKeyGenerated() {
		super();
	}
	
	

}
