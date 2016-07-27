package examples.spa.backend.myRest.meta;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class MyDatabaseMeta {
	public String tableTypes[] = new String[] {
			"TABLE",
			"VIEW",
			// "SYSTEM TABLE",
			"GLOBAL TEMPORARY",
			"LOCAL TEMPORARY",
			"ALIAS",
			"SYNONYM"
		};

	
	public Map<String, MyTableMeta> tables = new HashMap<>();
	
	public void load(DatabaseMetaData meta, String catalog, String schema) throws SQLException {
		List<MyTableMeta> tmp = new ArrayList<>();
		
		try (ResultSet rs = meta.getTables(catalog, schema, null, tableTypes)) {
			while (rs.next()) {
				MyTableMeta t = new MyTableMeta();
				t.catalog = rs.getString("TABLE_CAT");
				t.schema = rs.getString("TABLE_SCHEM");
				t.name = StringUtils.upperCase(rs.getString("TABLE_NAME"));
				t.tableType = rs.getString("TABLE_TYPE");
				t.remarks = rs.getString("REMARKS");

				t.typeCatalog = rs.getString("TYPE_CAT");
				t.typeSchema = rs.getString("TYPE_SCHEM");
				t.typeName = rs.getString("TYPE_NAME");
				t.selfReferencingColName = rs.getString("SELF_REFERENCING_COL_NAME");
				t.refGeneration = rs.getString("REF_GENERATION");
				
				tables.put(t.name, t); // Tables with equal names in different schemas/catalogs are not supported
				tmp.add(t);
			}
		}
		
		for (MyTableMeta table : tmp) {
			table.load(meta);
		}
	}
}