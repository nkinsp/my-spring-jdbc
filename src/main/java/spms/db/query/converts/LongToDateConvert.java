package spms.db.query.converts;

import java.util.Date;

import spms.db.query.QueryConvert;

public class LongToDateConvert implements QueryConvert<Date,Long>{

	@Override
	public Date convert(Long value) {
		// TODO Auto-generated method stub
		if(value == null) {
			return null;
		}
		return new Date(value);
	}

	

}
