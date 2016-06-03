package examples.spa.backend.component;

import java.io.Serializable;

import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import examples.spa.backend.model.EntityWithId;
import examples.spa.backend.model.Items;
import examples.spa.backend.model.ResultWrapper;

// @Controller
// @RequestMapping("/someItem")

/**
 * Ideas borrowed from:
 * https://gist.github.com/wvuong/5673644
 */
public abstract class EntityWithIdControllerBase<ID extends Serializable, T extends EntityWithId<ID>> {
	public static final String messageName = "message";
	
	public EntityWithIdJpa<ID, T> jpa;
	
	public EntityWithIdControllerBase(EntityWithIdJpa<ID, T> jpa) {
		this.jpa = jpa;
	}

	@RequestMapping(value="", method=RequestMethod.GET)
	public @ResponseBody Items<T> listItems(
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "20") int size) throws Exception {
		return new Items(jpa.list(page, size));
	}

	@RequestMapping(value="{id}", method=RequestMethod.GET)
	public @ResponseBody T loadItem(@PathVariable("id") ID id) throws Exception {
		return jpa.load(id);
	}

	@RequestMapping(value="{id}", method=RequestMethod.DELETE)
	public void deleteItem(@PathVariable("id") ID id) throws Exception {
		jpa.delete(id);
	}
	
	@RequestMapping(value="{id}", method=RequestMethod.POST)
	public @ResponseBody ResultWrapper<Integer> saveItem(@PathVariable("id") ID id,
			@RequestBody @Valid T item, BindingResult result) throws Exception {
		if (result.hasErrors() || (item == null) || (item.getId() == null) || (id != item.getId())) {
			return new ResultWrapper(0);
		} else {
			item = jpa.save(item);
			return new ResultWrapper(item.getId());
		}
	}

	@RequestMapping(value="new", method=RequestMethod.GET)
	public @ResponseBody T loadNewItem() throws Exception {
		return jpa.makeNew();
	}

	@RequestMapping(value="new", method=RequestMethod.POST)
	public @ResponseBody ResultWrapper<Integer> setNewItem(@RequestBody @Valid T item, BindingResult result) throws Exception {
		if (result.hasErrors() || (item == null) || (item.getId() != null)) {
			return new ResultWrapper(0);
		}
		item = jpa.save(item);
		return new ResultWrapper(item.getId());
	}
}
