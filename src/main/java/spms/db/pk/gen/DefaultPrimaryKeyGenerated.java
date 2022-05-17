package spms.db.pk.gen;

import java.util.UUID;

import spms.db.pk.PrimaryKeyGenerated;
import spms.db.table.ColumnField;
import spms.db.table.ColumnFieldType;
import spms.db.table.IdType;
import spms.db.table.TableMapping;

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
