package com.cement.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
/*
	@RequestMapping(value="{id}", method=RequestMethod.GET, produces={"text/html", "application/xml", "application/json"})
	protected String viewItem(Model model, RedirectAttributes redir,
			@PathVariable("id") Integer id) throws Exception {
		Object item = jpa.load(id);
		if (item == null) {
			redir.addFlashAttribute(messageName, "Item not found.");
			return "redirect:list";
		}
		model.addAttribute("model", item);
		return "SampleList";
	}*/
}
