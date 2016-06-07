package examples.spa.backend.misc;

import examples.spa.backend.model.FieldDef;

public class FieldDefinition {
	public final String name;
	
	public final boolean searchable;
	
	public final boolean ignoreCase;
	
	public final boolean appendWildcards;
	
	public final FieldDef.Order order;
	
	public final int orderPrecednce;
	
	public FieldDefinition(String name) {
		this.name = name;
		this.searchable = false;
		this.ignoreCase = true;
		this.appendWildcards = true;
		this.order = FieldDef.Order.NOT_SORTED;
		this.orderPrecednce = 0;
	}
	
	public FieldDefinition(String name, FieldDef fd) {
		this.name = name;
		this.searchable = fd.searchable();
		this.ignoreCase = fd.ignoreCase();
		this.appendWildcards = fd.appendWildcards();
		this.order = fd.order();
		this.orderPrecednce = fd.orderPrecednce();
	}
}
