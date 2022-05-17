package spms.db.util;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

/**
 * 实体工具类
 * 
 * @author yue
 *
 */
public class EntityUtils {

	/**
	 * 实体对象转为Map
	 * 
	 * @param entity
	 * @return
	 */
	private static Map<String, Object> getEntityToMap(Object entity) {
		synchronized (entity) {

			Map<String, Object> entityMap = new LinkedHashMap<>();

			Collection<PropertyDescriptor> descriptors = ClassUtils.getPropertyDescriptors(entity.getClass());
			for (PropertyDescriptor pd : descriptors) {
				try {
					Object value = pd.getReadMethod().invoke(entity);
					if (value != null) {
						entityMap.put(pd.getName(), value);
					}
				} catch (Exception e) {
					// TODO: handle exception
					throw new RuntimeException(e);
				}

			}
			return entityMap;
		}
	}

	/**
	 * 把实体转换为Map
	 * 
	 * @author hanjiang.Yue
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> entityToMap(Object entity) {
		if (entity == null) {
			return null;
		}
		if (Map.class.isAssignableFrom(entity.getClass())) {
			return (Map<String, Object>) entity;
		}
		return getEntityToMap(entity);
	}

	/**
	 * 实体List<T> 转为 List<Map<String, Object>>
	 * 
	 * @author hanjiang.Yue
	 * @param entitys
	 * @return
	 */
	public static <T> List<Map<String, Object>> entityListToMapList(List<T> entitys) {
		if (CollectionUtils.isEmpty(entitys)) {
			return new ArrayList<>(0);
		}
		return entitys.stream().map(EntityUtils::entityToMap).collect(Collectors.toList());
	}
}
