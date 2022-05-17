package spms.db.pk;

import spms.db.table.ColumnField;
import spms.db.table.TableMapping;

/**
 * 主键生成
 * @author hanjiang.Yue
 *
 */
public interface PrimaryKeyGenerated {

	/**
	 * 生成主键id
	 * @param idType
	 * @return
	 */
	ColumnField generated(TableMapping<?> mapping);
}
