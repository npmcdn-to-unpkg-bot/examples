package com.slavi.cement.page;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.click.control.Column;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Submit;
import org.apache.click.control.Table;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.DateField;
import org.apache.click.extras.control.DoubleField;
import org.apache.click.extras.control.FieldColumn;
import org.apache.click.extras.control.FormTable;
import org.apache.click.extras.control.IntegerField;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.lang.StringUtils;

import com.slavi.cement.service.Config;
import com.slavi.cement.service.Util;

public class SamplePage extends BorderPage {

	public Integer id;
	
	Form form = new Form("form");
	
	HiddenField idField = new HiddenField("sample_id", Integer.class);
	HiddenField mate_id = new HiddenField("mate_id", Integer.class);
	TextField client = new TextField("sample_place", "Client");
	DateField sampleDate = new DateField("sampleDate", "Sample date", true);
	DateField sampleMeasurementDate = new DateField("sampleDateMesr", "Measurement date", true);
	DoubleField density = new DoubleField("density", "Density", true);
	DoubleField weight = new DoubleField("weight", "Weight", true);
	DoubleField field1 = new DoubleField("field1", "Дробимост", false);
	DoubleField field2 = new DoubleField("field2", "Отмиваеми частици", false);
	
	FormTable table;
	
	public void onInit() {
		sampleDate.setFormatPattern("yyyy-MM-dd");
		sampleMeasurementDate.setFormatPattern("yyyy-MM-dd");
		
		form.add(idField);
		form.add(mate_id);
		form.add(client);
		form.add(sampleDate);
		form.add(sampleMeasurementDate);
		form.add(density);
		form.add(weight);
		form.add(field1);
		form.add(field2);
		form.add(new Submit("ok", "OK", this, "onOkClick"));
		form.add(new Submit("cancel", "Cancel", this, "onCancelClick"));
		form.add(new Submit("addPass", "Add pass", this, "onAddPassClick"));
		form.add(new Submit("removePass", "Remove last pass", this, "onRemovePassClick"));
		addControl(form);
		
		table = new FormTable("passesTable", form);
		table.setClass(Table.CLASS_SIMPLE);
		table.setPageSize(10);
		table.setShowBanner(true);
		table.setPageSize(0);
		form.add(table);

		table.restoreState(getContext());
	}

	public boolean onDummyClick() {
		return true;
	}
	
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	void createTableColumns(int sampleId) throws SQLException {
		String sql = "select distinct pass_id from material_sample_sieve_pass where sample_id=?";
		List<Integer> passes = Config.getInstance().getRunner().query(sql, new ColumnListHandler<Integer>(), sampleId);
		
		Column column = new Column("sieve_d", "");
		table.addColumn(column);
		for (Integer pass : passes) {
			FieldColumn fcolumn = new FieldColumn("pass_" + pass.toString(), "Пас " + (pass+1), new IntegerField("", "", 6));
			fcolumn.getField().setRequired(true);
			table.addColumn(fcolumn);
		}
	}
	
	ArrayList<Map> getTableRowsFromDB(int sampleId) {
		String sql = "select sieve_id, sieve_d from sieve";
		List<Map<String,Object>> sieves = Config.getInstance().queryAsMapList(sql);
		Map<Integer, Map> rows = new HashMap<>();
		for (Map<String,Object> sieve : sieves) {
			Map row = new HashMap(sieve);
			rows.put((Integer) sieve.get("sieve_id"), row);
		}
		
		sql = "select p.*, s.sieve_d " +
			"from material_sample_sieve_pass p join sieve s on p.sieve_id=s.sieve_id " +
			"where p.sample_id=? " +
			"order by p.pass_id, s.sieve_d";
		List<Map> items = (List) Config.getInstance().queryAsMapList(sql, sampleId);
		for (Map i : items) {
			try {
				Integer sieve_id = (Integer) i.get("sieve_id");
				Integer pass_id = (Integer) i.get("pass_id");
				Integer value = (Integer) i.get("value");
				Map row = rows.get(sieve_id);
				row.put("pass_" + pass_id, value);
			} catch (Exception e) {
				error(e);
			}
		}
		ArrayList<Map> rows_lst = new ArrayList(rows.values());
		Collections.sort(rows_lst, new Comparator<Map>() {
			public int compare(Map o1, Map o2) {
				return Integer.compare((Integer) o1.get("sieve_id"), (Integer) o2.get("sieve_id"));
			}
		});
		return rows_lst;
	}

	public void onGet() {
		if (id == null) {
			id = (Integer) idField.getValueObject();
		}
		if (id == null)
			return;
		
		Map item = Config.getInstance().queryAsMap("select * from material_sample where sample_id=?", id);
		form.copyFrom(item);
		try {
			sampleDate.setDate(df.parse((String) item.get("sample_date")));
			sampleMeasurementDate.setDate(df.parse((String) item.get("sample_date_mesr")));
			createTableColumns(id);
			table.setRowList(getTableRowsFromDB(id));
			table.saveState(getContext());
		} catch (Exception e) {
			error(e);
		}
	}
	
	public boolean onOkClick() {
		if (form.isValid()) {
			ArrayList param = new ArrayList();
			param.add(client.getValue());
			param.add(sampleDate.getValue());
			param.add(sampleMeasurementDate.getValue());
			param.add(density.getValue());
			param.add(weight.getValue());
			param.add(field1.getValue());
			param.add(field2.getValue());
			
			String sql;
			Connection conn = null;
			try {
				conn = Config.getInstance().getConnection();
				conn.setAutoCommit(false);
				
				if (StringUtils.isBlank(idField.getValue())) {
					if (StringUtils.isBlank(mate_id.getValue())) {
						error(new Exception("Material not set"));
						return true;
					}
					sql = "insert into material_sample(sample_place,sample_date,sample_date_mesr,density,weight,field1,field2,mate_id) values(?,?,?,?,?,?,?,?)";
					param.add(mate_id.getValue());
				} else {
					sql = "update material_sample set sample_place=?,sample_date=?,sample_date_mesr=?,density=?,weight=?,field1=?,field2=? where sample_id=?";
					param.add(idField.getValue());
				}
				Config.getInstance().getRunner().update(conn, sql, param.toArray());
				sql = "select sieve_id, sieve_d from sieve";
				List<Map<String,Object>> sieves = Config.getInstance().queryAsMapList(sql);
				sql = "delete from material_sample_sieve_pass where sample_id=?";
				Config.getInstance().getRunner().update(conn, sql, idField.getValue());
				sql = "insert into material_sample_sieve_pass(sample_id,sieve_id,pass_id,value) values(?,?,?,?)";
				List<Map<String, Object>> list = Util.extractFormTableValuesAsList("pass", getContext().getRequest().getParameterMap());
				for (int sieveNum = 0; sieveNum < list.size(); sieveNum++) {
					Map<String, Object> sieveRow = list.get(sieveNum);
					Integer sieveId = (Integer) sieveRow.get(Util.rowIdKey);
					sieveId = (Integer) sieves.get(sieveId).get("sieve_id");
					for (Map.Entry<String, Object> sieve : sieveRow.entrySet()) {
						if (Util.rowIdKey.equals(sieve.getKey()))
							continue;
						Object passId = sieve.getKey();
						Object value = sieve.getValue();
						Config.getInstance().getRunner().update(conn, sql, idField.getValue(), sieveId, passId, value);
					}
				}
				conn.commit();
				
/*				Map map = new HashMap<>();
				map.put("id", idField.getValue());
				setRedirect(this.getClass(), map);
				 */
				} catch (SQLException e) {
				DbUtils.rollbackAndCloseQuietly(conn);
				error(e);
			} finally {
				DbUtils.closeQuietly(conn);
			}
		}
		try {
			createTableColumns((Integer) idField.getValueObject());
			table.setRowList(getTableRowsFromDB((Integer) idField.getValueObject()));
		} catch (SQLException e) {
			error(e);
		}
		return true;
	}

	public boolean onAddPassClick() {
		String sql = "select max(pass_id) from material_sample_sieve_pass where sample_id=?";
		Integer passId = (Integer) Config.getInstance().queryAsObject(sql, idField.getValue());
		sql = "insert into material_sample_sieve_pass(sample_id,sieve_id,pass_id,value) select ?,sieve_id,?, 0 from sieve";
		int rowsAdded[];
		try {
			rowsAdded = Config.getInstance().getRunner().batch(sql, new Object[][]{{idField.getValue(), passId+1}});
/*			System.out.println("ROWS ADDED: " + Arrays.toString(rowsAdded));
			createTableColumns((Integer) idField.getValueObject());
			table.setRowList(getTableRowsFromDB((Integer) idField.getValueObject()));
*/
			Map map = new HashMap();
			map.put("id", idField.getValue());
			setRedirect(SamplePage.class, map);
		} catch (SQLException e) {
			error(e);
		}
		return true;
	}

	public boolean onRemovePassClick() {
		String sql = "delete from material_sample_sieve_pass where sample_id=? and pass_id=(select max(pass_id) from material_sample_sieve_pass where sample_id=?)";
		try {
			int rowsDeleted[] = Config.getInstance().getRunner().batch(sql, new Object[][]{{idField.getValue(), idField.getValue()}});
/*			System.out.println("ROWS DELETED: " + Arrays.toString(rowsDeleted));
			createTableColumns((Integer) idField.getValueObject());
			table.setRowList(getTableRowsFromDB((Integer) idField.getValueObject()));
*/
			Map map = new HashMap();
			map.put("id", idField.getValue());
			setRedirect(SamplePage.class, map);
		} catch (SQLException e) {
			error(e);
		}
		return true;
	}
	
	public boolean onCancelClick() {
		if (StringUtils.isBlank(mate_id.getValue())) {
			setRedirect(MaterialsPage.class);
		} else {
			Map map = new HashMap();
			map.put("id", mate_id.getValue());
			setRedirect(SamplesPage.class, map);
		}
		return false;
	}
}
