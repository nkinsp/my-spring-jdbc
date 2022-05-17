package spms.db.pk.gen;


import org.springframework.util.StringUtils;


import spms.db.table.ColumnField;
import spms.db.table.ColumnFieldType;
import spms.db.table.IdType;
import spms.db.table.TableMapping;

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
