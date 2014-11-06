package com.slavi.cement.page;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

import org.apache.click.Context;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Decorator;
import org.apache.click.control.Form;
import org.apache.click.control.PageLink;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.control.Table;
import org.apache.click.control.TextField;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang.StringUtils;

import com.slavi.cement.service.Config;
import com.slavi.cement.service.SimplePaginatingDataProvider;

public class MaterialsPage extends BorderPage {

	Form filter = new Form("filter");
	Select type = new Select("type", "Материал");
	Select size = new Select("size", "Размер");
	Select location = new Select("location", "Кариера");
	Select supplier = new Select("supplier", "Доставчик");
	Select region = new Select("region", "Регион");
	TextField comment = new TextField("comment", "Забележка");
	
	Table table = new Table("table");;

	PageLink viewLink = new PageLink("view", SamplesPage.class);
	PageLink editLink = new PageLink("edit", MaterialPage.class);
	ActionLink deleteLink = new ActionLink("delete", this, "onDeleteClick");
	
	public void onInit() {
		addControl(viewLink);
		addControl(editLink);
		addControl(deleteLink);
		
		filter.add(type);
		filter.add(size);
		filter.add(location);
		filter.add(supplier);
		filter.add(region);
		filter.add(comment);
		filter.add(new Submit("filter", "Filter"));
		filter.add(new Submit("clear", "Clear filter", this, "onClearFilterClick"));
		addControl(filter);

		type.setOptionList(Config.getInstance().queryAsOptionList("select type_id, type_name from mate_type order by 2", true));
		size.setOptionList(Config.getInstance().queryAsOptionList("select size_id, size_name from mate_size order by 2", true));
		location.setOptionList(Config.getInstance().queryAsOptionList("select geo_id, geo_name from mate_geo order by 2", true));
		supplier.setOptionList(Config.getInstance().queryAsOptionList("select supp_id, supp_name from mate_supplier order by 2", true));
		region.setOptionList(Config.getInstance().queryAsOptionList("select reg_id, reg_name from mate_reg order by 2", true));
		
		table.setClass(Table.CLASS_SIMPLE);
		table.setSortable(true);
		table.setPageSize(20);
		
		viewLink.setAttribute("class", "view");
		viewLink.setTitle("View channel details");
		editLink.setTitle("Edit channel details");
		editLink.setAttribute("class", "edit");
		deleteLink.setAttribute("class", "delete");
		deleteLink.setTitle("Delete channel record");
		deleteLink.setAttribute("onclick", "return window.confirm('Сигурни ли сте, че изкате да изтриете записа?');");
		
		Column column;
		column = new Column("action", "");
		column.setAttribute("class", "actions");
		column.setSortable(false);
		column.setDecorator(new Decorator() {
			public String render(Object row, Context context) {
				String id = String.valueOf(((Map)row).get("mate_id"));
				viewLink.setParameter("id", id);
				editLink.setParameter("id", id);
				deleteLink.setValue(id);
				return viewLink.toString() + "&nbsp;" + editLink.toString() + "&nbsp;" + deleteLink.toString();
			}
		});
		table.addColumn(column);
		
		column = new Column("type_name");
		column.setHeaderTitle("Материал");
		table.addColumn(column);
		table.setSortedColumn("type_name");

		column = new Column("size_name");
		column.setHeaderTitle("Размер");
		table.addColumn(column);
		
		column = new Column("geo_name");
		column.setHeaderTitle("Кариера");
		table.addColumn(column);

		column = new Column("supp_name");
		column.setHeaderTitle("Доставчик");
		table.addColumn(column);

		column = new Column("reg_name");
		column.setHeaderTitle("Регион");
		table.addColumn(column);

		column = new Column("comment");
		column.setHeaderTitle("Забележка");
		table.addColumn(column);

		addControl(table);
	}

	public void onRender() {
		ArrayList params = new ArrayList();
		String sql =
			"select mate_id, type_name, size_name, geo_name, supp_name, comment, reg_name " +
			"from material m " + 
				"join mate_type t on m.type_id=t.type_id " + 
				"join mate_size s on m.size_id=s.size_id " +
				"join mate_geo g on m.geo_id=g.geo_id " +
				"join mate_supplier supp on m.supp_id=supp.supp_id " + 
				"join mate_reg r on m.reg_id=r.reg_id ";
		String prefix = "where ";

		if (!StringUtils.isBlank(type.getValue())) {
			sql += prefix + " m.type_id=? ";
			params.add(type.getValue());
			prefix = "and ";
		}
		if (!StringUtils.isBlank(size.getValue())) {
			sql += prefix + " m.size_id=? ";
			params.add(size.getValue());
			prefix = "and ";
		}
		if (!StringUtils.isBlank(location.getValue())) {
			sql += prefix + " m.geo_id=? ";
			params.add(location.getValue());
			prefix = "and ";
		}
		if (!StringUtils.isBlank(supplier.getValue())) {
			sql += prefix + " m.supp_id=? ";
			params.add(supplier.getValue());
			prefix = "and ";
		}
		if (!StringUtils.isBlank(region.getValue())) {
			sql += prefix + " m.reg_id=? ";
			params.add(region.getValue());
			prefix = "and ";
		}
		if (!StringUtils.isBlank(comment.getValue())) {
			sql += prefix + " m.comment like ?";
			params.add("%" + comment.getValue() + "%");
			prefix = "and ";
		}
		
		table.setDataProvider(new SimplePaginatingDataProvider(table, sql, params.toArray()));
	}
	
	public boolean onClearFilterClick() {
		type.setValue("");
		size.setValue("");
		location.setValue("");
		supplier.setValue("");
		region.setValue("");
		comment.setValue("");
		return true;
	}
	
	public boolean onDeleteClick() {
		Connection conn = null;
		try {
			conn = Config.getInstance().getConnection();
			conn.setAutoCommit(false);
			String id = deleteLink.getValue();
			String sql;
			
			sql = "delete from material_sample_sieve_pass where sample_id in (select sample_id from material_sample where mate_id=?)";
			Config.getInstance().getRunner().update(conn, sql, id);
			sql = "delete from material_sample where mate_id=?";
			Config.getInstance().getRunner().update(conn, sql, id);
			sql = "delete from material where mate_id=?";
			Config.getInstance().getRunner().update(conn, sql, id);
			conn.commit();
			conn.close();
			setRedirect(this.getClass());
			return false;
		} catch (Exception e) {
			DbUtils.rollbackAndCloseQuietly(conn);
			error(e);
		}
		return true;
	}
}
