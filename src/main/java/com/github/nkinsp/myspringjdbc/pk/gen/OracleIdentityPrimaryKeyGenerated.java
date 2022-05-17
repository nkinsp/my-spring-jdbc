package com.github.nkinsp.myspringjdbc.pk.gen;


import org.springframework.util.StringUtils;

import com.github.nkinsp.myspringjdbc.table.ColumnField;
import com.github.nkinsp.myspringjdbc.table.ColumnFieldType;
import com.github.nkinsp.myspringjdbc.table.IdType;
import com.github.nkinsp.myspringjdbc.table.TableMapping;

/**
 * Oracle 自增 id
 * @author hanjiang.Yue
 */
public class OracleIdentityPrimaryKeyGenerated extends DefaultPrimaryKeyGenerated{

	
	
	@Override
	public ColumnField generated(TableMapping<?> mapping) {
		if(mapping.getIdType() == IdType.AUTO) {
			String keySequence = StringUtils.isEmpty(mapping.getKeySequence())?"sequence_id.nextval":mapping.getKeySequence();
			return new ColumnField(mapping.getPrimaryKey(), keySequence,ColumnFieldType.SQL);
		}
		return super.generated(mapping);
	}
}
