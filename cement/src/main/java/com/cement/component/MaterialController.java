package com.cement.component;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cement.model.Material;
import com.cement.model.MaterialType;

@Controller
@RequestMapping("/material")
public class MaterialController extends EntityWithIdControllerBase<Material> {
	@Autowired
	MaterialTypeJpa materialTypeJpa;
	
	@Autowired
	public MaterialController(MaterialJpa jpa) {
		super(jpa);
	}

	@ModelAttribute("materialTypes")
	Collection<MaterialType> getMaterialTypes() throws Exception {
		return materialTypeJpa.list();
	}
	
	protected String getViewItemEdit() {
		return "MaterialEdit";
	}

	protected String getViewItemList() {
		return "MaterialList";
	}
}
