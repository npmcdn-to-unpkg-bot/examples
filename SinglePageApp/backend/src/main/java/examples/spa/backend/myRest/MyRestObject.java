package examples.spa.backend.myRest;

import java.io.Serializable;

public class MyRestObject implements Serializable {

	String name;
	
	String table;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}
}
