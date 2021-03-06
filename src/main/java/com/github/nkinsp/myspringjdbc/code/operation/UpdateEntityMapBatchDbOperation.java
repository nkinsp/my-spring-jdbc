package com.github.nkinsp.myspringjdbc.code.operation;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import com.github.nkinsp.myspringjdbc.cache.CacheManager;
import com.github.nkinsp.myspringjdbc.query.Query;


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

		List<Object> ids = new ArrayList<Object>();

		List<Object[]> paramsArrays = this.data.stream().map(map -> {

			List<Object> param = new ArrayList<>();
			for (Entry<String, Object> en : map.entrySet()) {
				if (!en.getKey().equals(tableMapping.getPrimaryKey())) {
					param.add(en.getValue());
				}
			}
			Object id = map.get(tableMapping.getIdProperty().getFieldName());
			param.add(id);
			ids.add(id);
			return param.toArray();
		}).collect(Collectors.toList());

		if (tableMapping.isCache()) {
			CacheManager manager = context.getCacheManager();
			if (manager != null) {

				List<Object> keys = ids.stream()
						.map(x -> manager.getCacheKeyGenerated().generated(tableMapping.getTableClass(), x))
						.collect(Collectors.toList());
				manager.delete(keys);

			}
		}

		String sql = query.getSqlBuilder().buildUpdateSQL();

		if (log.isInfoEnabled() && context.isShowSqlInfo()) {
			log.info("==> execute batch [sql={}  size={} values={}]", sql, this.data.size(), this.data);
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
