package com.slavi.cement.page;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.click.Context;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Decorator;
import org.apache.click.control.Form;
import org.apache.click.control.PageLink;
import org.apache.click.control.Submit;
import org.apache.click.control.Table;
import org.apache.click.control.TextField;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.slavi.cement.service.Config;
import com.slavi.cement.service.SimplePaginatingDataProvider;
import com.slavi.util.Util;

public class SamplesPage extends BorderPage {

	public Integer id;
	
	Form form = new Form("form");
	
	TextField type = new TextField("type_name", "Материал");
	TextField size = new TextField("size_name", "Размер");
	TextField location = new TextField("geo_name", "Кариера");
	TextField supplier = new TextField("supp_name", "Доставчик");
	TextField region =  new TextField("reg_name", "Регион");
	TextField comment = new TextField("comment", "Забележка");

	Table table = new Table("table");
	
	ActionLink toggleLink = new ActionLink("toggle", this, "onToggleSampleClick");
	PageLink editLink = new PageLink("edit", SamplePage.class);
	ActionLink deleteLink = new ActionLink("delete", this, "onDeleteClick");
	
	public void onInit() {
		addControl(toggleLink);
		addControl(deleteLink);
		
		type.setDisabled(true);
		size.setDisabled(true);
		location.setDisabled(true);
		supplier.setDisabled(true);
		region.setDisabled(true);
		comment.setDisabled(true);
		
		form.add(type);
		form.add(size);
		form.add(location);
		form.add(supplier);
		form.add(region);
		form.add(comment);

		form.add(new Submit("cancel", "Back", this, "onCancelClick"));
		
		addControl(form);
		
		table.setClass(Table.CLASS_SIMPLE);
		table.setSortable(true);
		table.setPageSize(20);
		
		toggleLink.setTitle("View channel details");
		editLink.setAttribute("class", "view");
		editLink.setTitle("Edit channel details");
		deleteLink.setAttribute("class", "delete");
		deleteLink.setTitle("Delete channel record");
		deleteLink.setAttribute("onclick", "return window.confirm('Сигурни ли сте, че изкате да изтриете записа?');");

		Column column;
		column = new Column("action", "");
		column.setAttribute("class", "actions");
		column.setSortable(false);
		column.setDecorator(new Decorator() {
			public String render(Object row, Context context) {
				Map map = (Map) row;
				String sample_id = String.valueOf(map.get("sample_id"));
				toggleLink.setValue(sample_id);
				toggleLink.setLabel((Integer) map.get("sample_status") == 0 ? "[ ]" : "[x]"); 
				editLink.setParameter("id", sample_id);
				deleteLink.setValue(sample_id);
				return toggleLink.toString() + editLink.toString() + deleteLink.toString();
			}
		});
		table.addColumn(column);
		
		column = new Column("sample_status");
		column.setHeaderTitle("");
		column.setSortable(false);
		table.addColumn(column);

		column = new Column("sample_date");
		column.setHeaderTitle("Дата на измерване");
		table.addColumn(column);
		table.setSortedColumn("sample_date");
		
		column = new Column("sample_place");
		column.setHeaderTitle("Клиент");
		table.addColumn(column);

		column = new Column("density");
		column.setHeaderTitle("Плътност");
		table.addColumn(column);
		
		column = new Column("weight");
		column.setHeaderTitle("Насипно тегло");
		table.addColumn(column);
		
		column = new Column("module_size");
		column.setHeaderTitle("Модул на едрина");
		table.addColumn(column);

		column = new Column("field1");
		column.setHeaderTitle("Дробимост");
		table.addColumn(column);

		column = new Column("field2");
		column.setHeaderTitle("Отмиваеми частици");
		table.addColumn(column);

		column = new Column("sample_comment");
		column.setHeaderTitle("Бележки");
		table.addColumn(column);

		addControl(table);
	}

	public void onGet() {
		if (id != null) {
			String sql = 
					"select mate_id, type_name, size_name, geo_name, supp_name, comment, reg_name " +
					"from material m " + 
						"join mate_type t on m.type_id=t.type_id " + 
						"join mate_size s on m.size_id=s.size_id " +
						"join mate_geo g on m.geo_id=g.geo_id " +
						"join mate_supplier supp on m.supp_id=supp.supp_id " + 
						"join mate_reg r on m.reg_id=r.reg_id " +
					"where mate_id=? ";

			Map item = Config.getInstance().queryAsMap(sql, id);
			form.copyFrom(item);
			table.getControlLink().setParameter("id", id);
		}
	}
	
	public void onRender() {
		if (id == null)
			return;
		String sql = "select sample_id, sample_place, sample_date, sample_date_mesr, " +
				"sample_passnumb, sample_status, density, weight, field1, field2, " +
				"module_size, sample_comment " +
				"from material_sample where mate_id=? ";
		table.setDataProvider(new SimplePaginatingDataProvider(table, sql, id));
	}
	
	public boolean onCancelClick() {
		setRedirect(MaterialsPage.class);
		return false;
	}
	
	public boolean onToggleSampleClick() {
		Integer sample_id = toggleLink.getValueInteger();
		if (sample_id != null) {
			try {
				String sql = "update material_sample set sample_status=" + 
					"(select case sample_status when 0 then 2 else 0 end from material_sample where sample_id=?) where sample_id=?";
				Config.getInstance().getRunner().update(sql, sample_id, sample_id);
				id = (Integer) Config.getInstance().queryAsObject("select mate_id from material_sample where sample_id=?", sample_id);
				Map map = new HashMap();
				map.put("id", id);
				setRedirect(SamplesPage.class, map);
				return false;
			} catch (SQLException e) {
				addModel("error", Util.exceptionToString(e));
				log.error("", e);
			}
		}
		return true;
	}
	
	public boolean onDeleteClick() {
		Connection conn = null;
		try {
			conn = Config.getInstance().getConnection();
			conn.setAutoCommit(false);
			String id = deleteLink.getValue();
			String sql;
			
			sql = "select mate_id from material_sample where sample_id=?";
			Integer materialId = Config.getInstance().getRunner().query(conn, sql, new ScalarHandler<Integer>(), id);
			sql = "delete from material_sample_sieve_pass where sample_id=?";
			Config.getInstance().getRunner().update(conn, sql, id);
			sql = "delete from material_sample where sample_id=?";
			Config.getInstance().getRunner().update(conn, sql, id);
			conn.commit();
			conn.close();
			Map map = new HashMap();
			map.put("id", materialId);
			setRedirect(this.getClass(), map);
			return false;
		} catch (Exception e) {
			DbUtils.rollbackAndCloseQuietly(conn);
			error(e);
		}
		return true;
	}
}
