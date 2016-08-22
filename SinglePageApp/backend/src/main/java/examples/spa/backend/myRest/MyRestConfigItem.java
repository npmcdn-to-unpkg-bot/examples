package examples.spa.backend.myRest;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import examples.spa.backend.misc.Utils;

@Access(AccessType.FIELD)
public class MyRestConfigItem implements Serializable {
	
	String name;
	
	String sql;

	MyRestConfigField field[];
	
	@Transient
	@XmlTransient
	DbField dbField[];

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public String toString() {
		return Utils.objectToString(this);
	}

	public DbField[] getDbField() {
		return dbField;
	}

	public void setDbField(DbField[] dbField) {
		this.dbField = dbField;
	}

	public MyRestConfigField[] getField() {
		return field;
	}

	public void setField(MyRestConfigField[] field) {
		this.field = field;
	}
}
