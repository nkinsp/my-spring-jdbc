package com.github.nkinsp.myspringjdbc.code.operation;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import com.github.nkinsp.myspringjdbc.query.Query;
import com.github.nkinsp.myspringjdbc.table.ColumnField;


/**
 * 批量添加
 * 
 * @author hanjiangyue
 *
 * @param <T>
 */
public class InsertMapBatchDbOperation<T> extends InsertMapDbOperation<T> {

	private List<Map<String, Object>> data;

	public InsertMapBatchDbOperation(Query<T> query, List<Map<String, Object>> data) {
		super(query, null);
		this.data = data;
	}

	private List<List<ColumnField>> getBatchFields() {

		return this.data.stream().map(this::getColumnFields).collect(Collectors.toList());

	}

	@Override
	public Object dbAdapter() {

		List<List<ColumnField>> batchFields = getBatchFields();

		Map<String, Object> modelMap = getModelMap(batchFields.get(0));

		String sql = query.getSqlBuilder().buildInsertSQL(modelMap);

		final List<Object[]> paramsArrays = batchFields.stream()
				.map(fields -> fields.stream().map(ColumnField::getValue).toArray()).collect(Collectors.toList());

		if (log.isInfoEnabled() && context.isShowSqlInfo()) {
			log.info("==> execute batch [sql={} size={}]", sql, this.data.size());
		}

		return context.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Object[] params = paramsArrays.get(i);
				for (int j = 0; j < params.length; j++) {
					ps.setObject((j + 1), params[j]);
				}
			}

			@Override
			public int getBatchSize() {
				return paramsArrays.size();
			}
		});
	}

	

}
