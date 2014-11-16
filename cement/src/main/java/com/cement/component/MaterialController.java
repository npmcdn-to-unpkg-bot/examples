package com.cement.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cement.model.Material;

@Controller
@RequestMapping("/material")
public class MaterialController extends EntityWithIdControllerBase<Material> {
	@Autowired
	LocationJpa locationJpa;

	@Autowired
	MaterialTypeJpa materialTypeJpa;
	
	@Autowired
	MaterialSizeJpa materialSizeJpa;

	@Autowired
	RegionJpa regionJpa;

	@Autowired
	SupplierJpa supplierJpa;
	
	@Autowired
	public MaterialController(MaterialJpa jpa) {
		super(jpa);
	}

	@ModelAttribute
	void populateModel(Model model) throws Exception {
		model.addAttribute("locations", locationJpa.list());
		model.addAttribute("materialTypes", materialTypeJpa.list());
		model.addAttribute("materialSizes", materialSizeJpa.list());
		model.addAttribute("regions", regionJpa.list());
		model.addAttribute("suppliers", supplierJpa.list());
	}
	
	protected String getViewItemEdit() {
		return "MaterialEdit";
	}

	protected String getViewItemList() {
		return "MaterialList";
	}
}
