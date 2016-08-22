package examples.spa.backend.myRest;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import examples.spa.backend.misc.Utils;

public class DbField {

	// Info from RecordSetMetaData
	public boolean isAutoIncrement;
	public boolean isNullable;
	public boolean isSigned;
	public int columnDisplaySize;
	public String columnName;
	public int precision;
	public int scale;
	public int columnType;
	public String columnTypeName;
	public boolean isReadOnly;
	public String columnClassName;
	
	// Info from configuration file
	public boolean searchable = false;
	public boolean ignoreCase = true;
	public boolean appendWildcards = true;
	public SortOrder order = SortOrder.NONE;
	public int orderPrecednce = 0;
	
	public void assignFromRSMetaData(ResultSetMetaData m, int column) throws SQLException {
		isAutoIncrement = m.isAutoIncrement(column);
		isNullable = m.isNullable(column) == ResultSetMetaData.columnNullable;
		isSigned = m.isSigned(column);
		columnDisplaySize = m.getColumnDisplaySize(column);
		columnName = m.getColumnLabel(column); // Not a bug
		precision = m.getPrecision(column);
		scale = m.getScale(column);
		columnType = m.getColumnType(column);
		columnTypeName = m.getColumnTypeName(column);
		isReadOnly = !m.isDefinitelyWritable(column);
		columnClassName = m.getColumnName(column);
	}
	
	public void assignFromConfigField(MyRestConfigField field) {
		searchable = field.searchable;
		ignoreCase = field.ignoreCase;
		appendWildcards = field.appendWildcards;
		order = Utils.nvl(field.order, SortOrder.NONE);
	}
}
