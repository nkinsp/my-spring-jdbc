package spms.db.code.operation;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;


import spms.db.query.Query;


/**
 * 批量添加
 * 
 * @author hanjiangyue
 *
 * @param <T>
 */
public class UpdateEntityMapBatchDbOperation<T> extends UpdateEntityMapDbOperation<T> {

	private List<Map<String, Object>> data;

	private Map<String, Object> parsetModelMap(Map<String, Object> map) {

		Map<String, Object> model = new HashMap<String, Object>();

		for (Entry<String, Object> en : map.entrySet()) {

			model.put(tableMapping.getColumnName(en.getKey()), en.getValue());
		}

		return model;

	}
	
	public UpdateEntityMapBatchDbOperation(Query<T> query, List<Map<String, Object>> data) {
		super(query,data.get(0));
		this.data = data.stream().map(this::parsetModelMap).collect(Collectors.toList());
	}
	
	
	
	
	
	
	
	

	@Override
	public Object dbAdapter() {

		String sql = query.getSqlBuilder().buildUpdateSQL();

		List<Object[]> paramsArrays = this.data.stream().map(map -> {

			List<Object> param = new ArrayList<>();
			for (Entry<String, Object> en : map.entrySet()) {
				if (!en.getKey().equals(tableMapping.getPrimaryKey())) {
					param.add(en.getValue());
				}
			}
			param.add(map.get(tableMapping.getPrimaryKey()));

			return param.toArray();
		}).collect(Collectors.toList());

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
