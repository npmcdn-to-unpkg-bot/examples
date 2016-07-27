package examples.spa.backend.myRest.meta;

import java.util.ArrayList;
import java.util.List;

public class MyTabForegnKey {
	public String name;
	public String pkTableCatalog;
	public String pkTableSchema;
	public String pkTableName;
	public List<String> columns = new ArrayList<>();
	public List<String> pkColumns = new ArrayList<>();

	public int updateRule;
	public int deleteRule;
	public int deferrability;
}