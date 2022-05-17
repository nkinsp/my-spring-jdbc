package com.github.nkinsp.myspringjdbc.pk;

import com.github.nkinsp.myspringjdbc.table.ColumnField;
import com.github.nkinsp.myspringjdbc.table.TableMapping;

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
