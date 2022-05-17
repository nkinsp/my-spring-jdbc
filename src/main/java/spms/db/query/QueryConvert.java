package spms.db.query;

public interface QueryConvert<T extends Object,R extends Object> {

	
	
	T convert(R value);
	
}
