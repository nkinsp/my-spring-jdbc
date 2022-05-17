package spms.db.code;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import spms.db.table.TableMapping;

public interface AnnotationOperation {

	
	
	boolean match(Class<? extends Annotation> annotationClass);
	
	<R> R execute(Method method,DbContext context,TableMapping<?> table);
	
}
