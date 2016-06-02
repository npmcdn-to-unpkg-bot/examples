package examples.spa.backend.component;

import javax.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import examples.spa.backend.model.EntityWithId;
import examples.spa.backend.model.Items;
import examples.spa.backend.model.ResultWrapper;

// @Controller
// @RequestMapping("/someItem")
public abstract class EntityWithIdControllerBase<T extends EntityWithId> {
	public static final String messageName = "message";
	
	protected EntityWithIdJpa<T> jpa;
	
	public EntityWithIdControllerBase(EntityWithIdJpa<T> jpa) {
		this.jpa = jpa;
	}

	@RequestMapping(value="", method=RequestMethod.GET)
	@ResponseBody
	protected Items<T> list() throws Exception {
		return new Items(jpa.list());
	}

	@RequestMapping(value="{id}", method=RequestMethod.DELETE)
	protected void deleteItem(@PathVariable("id") int id) throws Exception {
		jpa.delete(id);
	}
	
	@RequestMapping(value="{id}", method=RequestMethod.GET)
	@ResponseBody
	protected T loadItem(@PathVariable("id") int id) throws Exception {
		return jpa.load(id);
	}

	@RequestMapping(value="new", method=RequestMethod.GET)
	@ResponseBody
	protected T loadNewItem(Model model) throws Exception {
		return jpa.makeNew();
	}

	@RequestMapping(value="{id}", method=RequestMethod.POST)
	@ResponseBody
	protected ResultWrapper<Integer> saveItem(@PathVariable("id") int id,
			@RequestBody T item) throws Exception {
		System.out.println(item);
		if ((item == null) || (item.getId() == null) || (id != item.getId())) {
			return new ResultWrapper(0);
		} else {
			item = jpa.save(item);
			return new ResultWrapper(item.getId());
		}
	}

	@RequestMapping(value="new", method=RequestMethod.POST)
	@ResponseBody
	protected ResultWrapper<Integer> setNewItem(@Valid T item, BindingResult result) throws Exception {
		if (result.hasErrors() || (item == null)) {
			return new ResultWrapper(0);
		}
		item = jpa.save(item);
		return new ResultWrapper(item.getId());
	}
}
