package com.cement.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cement.model.Material;

@Controller
@RequestMapping("/material")
public class MaterialController extends EntityWithIdControllerBase<Material> {
	@Autowired
	public MaterialController(MaterialJpa jpa) {
		super(jpa);
	}

	protected String getViewItemEdit() {
		return "MaterialEdit";
	}

	protected String getViewItemList() {
		return "MaterialList";
	}
}
