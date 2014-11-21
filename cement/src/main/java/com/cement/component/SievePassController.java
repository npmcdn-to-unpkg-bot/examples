package com.cement.component;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cement.misc.SievePassForm;
import com.cement.model.IdNamePair;
import com.cement.model.Sample;

@Controller
@RequestMapping("/material/{materialId}/{sampleId}")
public class SievePassController {
	public static final String messageName = "message";

	@Autowired
	SieveJpa sieveJpa;
	
	@Autowired
	SampleJpa sampleJpa;

	@Autowired
	SievePassJpa jpa;

	@ModelAttribute("title")
	String getTitle() {
		return "SievePass";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		dateFormat.setLenient(false);
//		binder.registerCustomEditor(requiredType, propertyEditor);
//		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
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
		model.addAttribute("sieves", jpa.listSieves());
		model.addAttribute("passes", jpa.listPassIds(sampleId));
		model.addAttribute("model", jpa.getSampleData(sampleId));
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
			@ModelAttribute("model") @Valid SievePassForm data, BindingResult result) throws Exception {
			// @Valid StringMapForm items, BindingResult result) throws Exception {
		Sample dbSample = sampleJpa.load(sampleId);
		Collection<IdNamePair> sieves = jpa.listSieves();
		if (result.hasErrors() || (dbSample == null) || (dbSample.getMaterial().getId() != materialId) ||
				(data == null) || (data.getValues() == null) || (data.getValues().size() != sieves.size())) {
			model.addAttribute(messageName, "Oooops");
			model.addAttribute("sieves", sieves);
			//model.addAttribute("model", result);
			return getViewItemEdit();
		}
		System.out.println(data);
//		System.out.println(request.getHeaderNames());
//		System.out.println(request.getParameterMap());
		jpa.save(sampleId, id, data);
		redir.addFlashAttribute(messageName, "Data saved.");
		return "redirect:passes";
	}

	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces={"text/html", "application/xml", "application/json"})
	protected String loadItem(Model model, RedirectAttributes redir,
			@PathVariable("materialId") int materialId,
			@PathVariable("sampleId") int sampleId,
			@PathVariable("id") Integer id) throws Exception {
		Sample item = sampleJpa.load(sampleId);
		model.addAttribute("sieves", jpa.listSieves());
		if ((item == null) ||
			(item.getMaterial().getId() != materialId)) {
			redir.addFlashAttribute(messageName, "Item not found. Creating new.");
			return "redirect:new";
		}
		model.addAttribute("model", jpa.load(sampleId, id));
		return getViewItemEdit();
	}

	@RequestMapping(value="/new", method=RequestMethod.GET)
	protected String loadNewItem(Model model) throws Exception {
		model.addAttribute("sieves", jpa.listSieves());
		model.addAttribute("model", jpa.makeNew());
		return getViewItemEdit();
	}

	@Transactional
	@RequestMapping(value="/new", method=RequestMethod.POST)
	protected String setNewItem(Model model, RedirectAttributes redir,
			@PathVariable("materialId") int materialId,
			@PathVariable("sampleId") int sampleId,
			@ModelAttribute("model") @Valid SievePassForm data, BindingResult result) throws Exception {
		Sample dbSample = sampleJpa.load(sampleId);
		Collection<IdNamePair> sieves = jpa.listSieves();
		if (result.hasErrors() || (dbSample == null) || (dbSample.getMaterial().getId() != materialId) ||
				(data == null) || (data.getValues() == null) || (data.getValues().size() != sieves.size())) {
			model.addAttribute(messageName, "Oooops");
			model.addAttribute("sieves", jpa.listSieves());
			//model.addAttribute("model", result);
			return getViewItemEdit();
		}
		jpa.save(sampleId, -1, data);
		redir.addFlashAttribute(messageName, "Data saved.");
		return "redirect:passes";
	}
}
