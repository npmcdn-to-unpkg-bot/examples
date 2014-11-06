package com.slavi.cement.page;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.commons.lang.StringUtils;

import com.slavi.cement.service.Config;
import com.slavi.util.Util;

public class MaterialPage extends BorderPage {

	public Integer id;
	
	Form form = new Form("form");
	
	HiddenField idField = new HiddenField("mate_id", Integer.class);
	Select type = new Select("type_id", "Материал", true);
	Select size = new Select("size_id", "Размер", true);
	Select location = new Select("geo_id", "Кариера", true);
	Select supplier = new Select("supp_id", "Доставчик", true);
	Select region = new Select("reg_id", "Регион", true);
	TextField comment = new TextField("comment", "Забележка");

	public void onInit() {
		form.add(idField);
		form.add(type);
		form.add(size);
		form.add(location);
		form.add(supplier);
		form.add(region);
		form.add(comment);
		form.add(new Submit("ok", "Запис", this, "onOkClick"));
		form.add(new Submit("cancel", "Отказ", this, "onCancelClick"));
		addControl(form);

		type.setOptionList(Config.getInstance().queryAsOptionList("select type_id, type_name from mate_type order by 2", true));
		size.setOptionList(Config.getInstance().queryAsOptionList("select size_id, size_name from mate_size order by 2", true));
		location.setOptionList(Config.getInstance().queryAsOptionList("select geo_id, geo_name from mate_geo order by 2", true));
		supplier.setOptionList(Config.getInstance().queryAsOptionList("select supp_id, supp_name from mate_supplier order by 2", true));
		region.setOptionList(Config.getInstance().queryAsOptionList("select reg_id, reg_name from mate_reg order by 2", true));
	}

	public void onGet() {
		if (id != null) {
			Map item = Config.getInstance().queryAsMap("select * from material where mate_id=?", id);
			form.copyFrom(item);
		}
	}
	
	public boolean onOkClick() {
		if (form.isValid()) {
			ArrayList param = new ArrayList();
			param.add(type.getValue());
			param.add(size.getValue());
			param.add(location.getValue());
			param.add(supplier.getValue());
			param.add(region.getValue());
			param.add(StringUtils.trimToEmpty(comment.getValue()));
			
			String sql;
			if (StringUtils.isBlank(idField.getValue())) {
				sql = "insert into material(type_id,size_id,geo_id,supp_id,reg_id,comment) values(?,?,?,?,?,?)";
			} else {
				sql = "update material set type_id=?,size_id=?,geo_id=?,supp_id=?,reg_id=?,comment=? where mate_id=?";
				param.add(idField.getValue());
			}
			try {
				Config.getInstance().getRunner().update(sql, param.toArray());
				setRedirect(MaterialsPage.class);
			} catch (SQLException e) {
				addModel("error", Util.exceptionToString(e));
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public boolean onCancelClick() {
		setRedirect(MaterialsPage.class);
		return false;
	}
}
