package examples.spa.backend.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface FieldDef {
	public enum Order {
		ASC, DESC, NOT_SORTED
	}
	
	boolean searchable() default false;
	
	boolean ignoreCase() default true;
	
	boolean appendWildcards() default true;
	
	Order order() default Order.NOT_SORTED;
	
	/**
	 * Precedence in the order by list. The higher the number the higher in the order by list. 
	 * If more than one have the same precedence number then the "final" precedence is determined in
	 * alphabetical order.
	 */
	int orderPrecednce() default 0;
}
