package com.cement.component;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cement.model.Material;
import com.cement.model.Sample;

@Controller
@RequestMapping("/material/{materialId}")
public class SampleController {
	public static final String messageName = "message";

	@Autowired
	MaterialJpa materialJpa;

	@Autowired
	SampleJpa jpa;

	@ModelAttribute("title")
	String getTitle() {
		return jpa.getEntityClass().getSimpleName();
	}
	
	@ModelAttribute
	void populateModel(Model model, @PathVariable("materialId") int materialId) throws Exception {
		model.addAttribute("material", materialJpa.load(materialId));
	}
	
	protected String getViewItemEdit() {
		return "SampleEdit";
	}

	protected String getViewItemList() {
		return "SampleList";
	}
	
	@RequestMapping(value="/samples", method=RequestMethod.GET, produces={"text/html", "application/xml", "application/json"})
	protected String list(Model model, @ModelAttribute("material") Material material) throws Exception {
		model.addAttribute("model", material.getSamples());
		return getViewItemList();
	}

	@RequestMapping(value="/{id}/delete")
	protected String deleteItem(ModelMap model, RedirectAttributes redir,
			@PathVariable("materialId") int materialId,
			@PathVariable("id") int id) throws Exception {
		Sample item = jpa.load(id);
		if ((item != null) && 
			(item.getMaterial().getId() == materialId)) {
			jpa.delete(id);
		}
		return "redirect:../samples";
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.POST)
	protected String saveItem(Model model, RedirectAttributes redir,
			@PathVariable("materialId") int materialId,
			@PathVariable("id") int id,
			@Valid Sample item, BindingResult result) throws Exception {
		Sample dbSample = jpa.load(id);
		if (result.hasErrors() || (item == null) || (item.getId() == null) || (id != item.getId()) ||
			(dbSample == null) ||
			(dbSample.getMaterial().getId() != materialId)) {
			model.addAttribute(messageName, "Oooops");
			return getViewItemEdit();
		}
		item.setMaterial(dbSample.getMaterial());
		jpa.save(item);
		redir.addFlashAttribute(messageName, "Data saved.");
		return "redirect:samples";
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces={"text/html", "application/xml", "application/json"})
	protected String loadItem(Model model, RedirectAttributes redir,
			@PathVariable("materialId") int materialId,
			@PathVariable("id") Integer id) throws Exception {
		Sample item = jpa.load(id);
		if ((item == null) ||
			(item.getMaterial().getId() != materialId)) {
			redir.addFlashAttribute(messageName, "Item not found. Creating new.");
			return "redirect:new";
		}
		model.addAttribute("model", item);
		return getViewItemEdit();
	}

	@RequestMapping(value="/new", method=RequestMethod.GET)
	protected String loadNewItem(Model model) throws Exception {
		Object item = jpa.makeNew();
		model.addAttribute("model", item);
		return getViewItemEdit();
	}

	@Transactional
	@RequestMapping(value="/new", method=RequestMethod.POST)
	protected String setNewItem(Model model, RedirectAttributes redir,
			@ModelAttribute("material") Material material,
			@Valid Sample item, BindingResult result) throws Exception {
		if (result.hasErrors() || (item == null)) {
			model.addAttribute(messageName, "Oooops");
			return getViewItemEdit();
		}
		item.setMaterial(material);
		jpa.save(item);
		redir.addFlashAttribute(messageName, "Data saved.");
		return "redirect:samples";
	}
}
