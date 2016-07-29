package examples.spa.backend.myRest.meta;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class MyTableMeta {
	public String catalog;
	public String schema;
	public String name;
	public String tableType;
	public String remarks;
	
	public String typeCatalog;
	public String typeSchema;
	public String typeName;
	public String selfReferencingColName;
	public String refGeneration;
	
	public Map<String, MyTabColumnMeta> columns = new HashMap<>();
	public List<String> primaryKeyColumns = new ArrayList<>();
	public List<String> bestRowIdColumns = new ArrayList<>();
	public Set<String> bestRowIdColumnsSet = new HashSet<>();
	public Map<String, MyTabForegnKey> foreignKeys = new HashMap<>();
	public Map<String, MyTabIndex> indexes = new HashMap<>();

	public void load(DatabaseMetaData meta) throws SQLException {
		// Read table columns
		try (ResultSet rs = meta.getColumns(catalog, schema, name, null)) {
			while (rs.next()) {
				MyTabColumnMeta c = new MyTabColumnMeta();
				c.name = StringUtils.upperCase(rs.getString("COLUMN_NAME"));
				c.dataType = rs.getInt("DATA_TYPE");
				c.typeName = rs.getString("TYPE_NAME");
				c.columnSize = rs.getInt("COLUMN_SIZE");
				c.bufferLength = rs.getInt("BUFFER_LENGTH");
				c.decimalDigits = rs.getInt("DECIMAL_DIGITS");
				c.numPrecRadix = rs.getInt("NUM_PREC_RADIX");
				c.remarks = rs.getString("REMARKS");
				c.columnDef = rs.getString("COLUMN_DEF");
				c.sqlDataType = rs.getInt("SQL_DATA_TYPE");
				c.sqlDateTimeSub = rs.getInt("SQL_DATETIME_SUB");
				c.charOctetLength = rs.getInt("CHAR_OCTET_LENGTH");
				c.ordinalPosition = rs.getInt("ORDINAL_POSITION");
				c.isNullable = "YES".equalsIgnoreCase(rs.getString("IS_NULLABLE"));
				c.isAutoincrement = "YES".equalsIgnoreCase(rs.getString("IS_AUTOINCREMENT"));
				c.isGeneratedColumn = "YES".equalsIgnoreCase(rs.getString("IS_GENERATEDCOLUMN"));
				columns.put(c.name, c);
			}
		}
		
		try (ResultSet rs = meta.getPrimaryKeys(catalog, schema, name)) {
			while (rs.next()) {
				String columnName = StringUtils.upperCase(rs.getString("COLUMN_NAME"));
				primaryKeyColumns.add(columnName);
			}
		}
		
		try (ResultSet rs = meta.getBestRowIdentifier(catalog, schema, name, 0, true)) {
			while (rs.next()) {
				String columnName = StringUtils.upperCase(rs.getString("COLUMN_NAME"));
				bestRowIdColumns.add(columnName);
				bestRowIdColumnsSet.add(columnName);
			}
		}
		
		try (ResultSet rs = meta.getImportedKeys(catalog, schema, name)) {
			while (rs.next()) {
				String fkName = StringUtils.upperCase(rs.getString("FK_NAME"));
				MyTabForegnKey fk = foreignKeys.get(fkName);
				if (fk == null) {
					fk = new MyTabForegnKey();
					fk.name = fkName;
					fk.pkTableCatalog = rs.getString("PKTABLE_CAT");
					fk.pkTableSchema = rs.getString("PKTABLE_SCHEM");
					fk.pkTableName = rs.getString("PKTABLE_NAME");
					fk.updateRule = rs.getInt("UPDATE_RULE");
					fk.deleteRule = rs.getInt("DELETE_RULE");
					fk.deferrability = rs.getInt("DEFERRABILITY");
					foreignKeys.put(fkName, fk);
				}
				String fkColumnName = StringUtils.upperCase(rs.getString("FKCOLUMN_NAME"));
				fk.columns.add(fkColumnName);
				fk.pkColumns.add(StringUtils.upperCase(rs.getString("PKCOLUMN_NAME")));
				foreignKeys.put(fk.name, fk);
			}
		}
		
		try (ResultSet rs = meta.getIndexInfo(catalog, schema, name, false, false)) {
			while (rs.next()) {
				String indexName = StringUtils.upperCase(rs.getString("INDEX_NAME"));
				MyTabIndex index = indexes.get(indexName);
				if (index == null) {
					index = new MyTabIndex();
					index.name = indexName;
					if (Boolean.FALSE.equals(rs.getBoolean("NON_UNIQUE"))) {
						index.isUnique = true;
					};
					indexes.put(indexName, index);
				}
				String columnName = StringUtils.upperCase(rs.getString("COLUMN_NAME"));
				if (!index.columns.contains(columnName))
					index.columns.add(columnName);
			}
		}
	}
}