package spms.db.code;

/**
 * 数据库操作执行
 * @author hanjiang.Yue
 *
 */
public interface DbOperation{

	
	<R> R execute();
	
}
