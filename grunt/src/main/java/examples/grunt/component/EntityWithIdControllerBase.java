package examples.grunt.component;

import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import examples.grunt.model.EntityWithId;
import examples.grunt.model.Items;

// @Controller
// @RequestMapping("/someItem")
public abstract class EntityWithIdControllerBase<T extends EntityWithId> {
	public static final String messageName = "message";
	
	protected EntityWithIdJpa<T> jpa;
	
	public EntityWithIdControllerBase(EntityWithIdJpa<T> jpa) {
		this.jpa = jpa;
	}

	@RequestMapping(value="", method=RequestMethod.GET, produces={"application/xml", "application/json"})
	protected Items<T> list() throws Exception {
		return new Items(jpa.list());
	}

	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	protected void deleteItem(@PathVariable("id") int id) throws Exception {
		jpa.delete(id);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.POST)
	protected Integer saveItem(@PathVariable("id") int id,
			@Valid T item, BindingResult result) throws Exception {
		if (result.hasErrors() || (item == null) || (item.getId() == null) || (id != item.getId())) {
			return 0;
		} else {
			item = jpa.save(item);
			return item.getId();
		}
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces={"application/xml", "application/json"})
	protected T loadItem(@PathVariable("id") int id) throws Exception {
		return jpa.load(id);
	}

	@RequestMapping(value="/new", method=RequestMethod.GET, produces={"application/xml", "application/json"})
	protected T loadNewItem(Model model) throws Exception {
		return jpa.makeNew();
	}

	@Transactional
	@RequestMapping(value="/new", method=RequestMethod.POST)
	protected Integer setNewItem(@Valid T item, BindingResult result) throws Exception {
		if (result.hasErrors() || (item == null)) {
			return 0;
		}
		item = jpa.save(item);
		return item.getId();
	}
}
