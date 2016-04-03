package examples.grunt.component;

import javax.validation.Valid;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import examples.grunt.model.EntityWithId;

// @Controller
// @RequestMapping("/someItem")
public abstract class EntityWithIdControllerBase<T extends EntityWithId> {
	public static final String messageName = "message";
	
	protected EntityWithIdJpa<T> jpa;
	
	public EntityWithIdControllerBase(EntityWithIdJpa<T> jpa) {
		this.jpa = jpa;
	}

	@ModelAttribute("title")
	String getTitle() {
		return jpa.getEntityClass().getSimpleName();
	}
	
	protected abstract String getViewItemEdit();
	protected abstract String getViewItemList();

	@RequestMapping(value="", method=RequestMethod.GET, produces={"text/html", "application/xml", "application/json"})
	protected String list(Model model) throws Exception {
		model.addAttribute("model", jpa.list());
		return getViewItemList();
	}

	@RequestMapping(value="/{id}/delete")
	protected String deleteItem(ModelMap model, RedirectAttributes redir,
			@PathVariable("id") int id) throws Exception {
		jpa.delete(id);
		return "redirect:..";
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.POST)
	protected String saveItem(Model model, RedirectAttributes redir,
			@PathVariable("id") int id,
			@Valid T item, BindingResult result) throws Exception {
		if (result.hasErrors() || (item == null) || (item.getId() == null) || (id != item.getId())) {
			model.addAttribute(messageName, "Oooops");
			return getViewItemEdit();
		}
		jpa.save(item);
		redir.addFlashAttribute(messageName, "Data saved.");
		return "redirect:.";
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces={"text/html", "application/xml", "application/json"})
	protected String loadItem(Model model, RedirectAttributes redir,
			@PathVariable("id") int id) throws Exception {
		Object item = jpa.load(id);
		if (item == null) {
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
			@Valid T item, BindingResult result) throws Exception {
		if (result.hasErrors() || (item == null)) {
			model.addAttribute(messageName, "Oooops");
			return getViewItemEdit();
		}
		jpa.save(item);
		redir.addFlashAttribute(messageName, "Data saved.");
		return "redirect:.";
	}
}
