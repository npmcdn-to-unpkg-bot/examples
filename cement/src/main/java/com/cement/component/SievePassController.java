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
import com.cement.model.SievePass;

@Controller
@RequestMapping("/material/{materialId}/{sampleId}")
public class SievePassController {
	public static final String messageName = "message";

	@Autowired
	SampleJpa sampleJpa;

	@Autowired
	SievePassJpa jpa;

	@ModelAttribute("title")
	String getTitle() {
		return SievePass.class.getSimpleName();
	}
	
	@ModelAttribute
	void populateModel(Model model, 
			@PathVariable("materialId") int materialId,
			@PathVariable("sampleId") int sampleId) throws Exception {
		Sample sample = sampleJpa.load(sampleId);
		if (sample.getMaterial().getId() != materialId)
			throw new Exception("Invalid arguments");
		model.addAttribute("sample", sample);
		model.addAttribute("material", sample.getMaterial());
	}
	
	protected String getViewItemEdit() {
		return "SievePassEdit";
	}

	protected String getViewItemList() {
		return "SievePassList";
	}
	
	@RequestMapping(value="/passes", method=RequestMethod.GET, produces={"text/html", "application/xml", "application/json"})
	protected String list(Model model, @PathVariable("sampleId") int sampleId) throws Exception {
		model.addAttribute("passes", sampleJpa.getSievePasses(sampleId));
		model.addAttribute("model", sampleJpa.getSampleData(sampleId));
		return getViewItemList();
	}

	@RequestMapping(value="/delete/{id}")
	protected String deleteItem(ModelMap model, RedirectAttributes redir,
			@PathVariable("materialId") int materialId,
			@PathVariable("sampleId") int sampleId,
			@PathVariable("id") int id) throws Exception {
		Sample item = sampleJpa.load(sampleId);
		if ((item != null) && 
			(item.getMaterial().getId() == materialId)) {
			jpa.delete(sampleId, id);
		}
		return "redirect:../passes";
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.POST)
	protected String saveItem(Model model, RedirectAttributes redir,
			@PathVariable("materialId") int materialId,
			@PathVariable("sampleId") int sampleId,
			@PathVariable("id") int id,
			@Valid Sample item, BindingResult result) throws Exception {
		// TODO:
		Sample dbSample = sampleJpa.load(sampleId);
		if (result.hasErrors() || (dbSample == null) || (dbSample.getMaterial().getId() != sampleId)) {
			model.addAttribute(messageName, "Oooops");
			return getViewItemEdit();
		}
		sampleJpa.save(item);
		redir.addFlashAttribute(messageName, "Data saved.");
		return "redirect:passes";
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces={"text/html", "application/xml", "application/json"})
	protected String loadItem(Model model, RedirectAttributes redir,
			@PathVariable("materialId") int materialId,
			@PathVariable("sampleId") int sampleId,
			@PathVariable("id") Integer id) throws Exception {
		Sample item = sampleJpa.load(id);
		if ((item == null) ||
			(item.getMaterial().getId() != materialId)) {
			redir.addFlashAttribute(messageName, "Item not found. Creating new.");
			return "redirect:new";
		}
		sampleJpa.loadPass(sampleId, id);
		model.addAttribute("model", item);
		return getViewItemEdit();
	}

	@RequestMapping(value="/new", method=RequestMethod.GET)
	protected String loadNewItem(Model model) throws Exception {
		Object item = sampleJpa.makeNew();
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
		sampleJpa.save(item);
		redir.addFlashAttribute(messageName, "Data saved.");
		return "redirect:.";
	}
}
