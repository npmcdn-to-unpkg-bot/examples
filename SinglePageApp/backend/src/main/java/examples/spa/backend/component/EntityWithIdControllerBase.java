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

// Read more: https://gist.github.com/wvuong/5673644
public abstract class EntityWithIdControllerBase<T extends EntityWithId> implements CrudControllerInterface<T>{
	public static final String messageName = "message";
	
	public EntityWithIdJpa<T> jpa;
	
	public EntityWithIdControllerBase(EntityWithIdJpa<T> jpa) {
		this.jpa = jpa;
	}

	public Items<T> list() throws Exception {
		System.out.println("List items");
		return new Items(jpa.list());
	}

	public void deleteItem(@PathVariable("id") int id) throws Exception {
		jpa.delete(id);
	}
	
	public T loadItem(@PathVariable("id") int id) throws Exception {
		return jpa.load(id);
	}

	public T loadNewItem(Model model) throws Exception {
		return jpa.makeNew();
	}

	public ResultWrapper<Integer> saveItem(@PathVariable("id") int id,
			@RequestBody T item) throws Exception {
		System.out.println(item);
		if ((item == null) || (item.getId() == null) || (id != item.getId())) {
			return new ResultWrapper(0);
		} else {
			item = jpa.save(item);
			return new ResultWrapper(item.getId());
		}
	}

	public ResultWrapper<Integer> saveItem2(
			@RequestBody T item) throws Exception {
		System.out.println(item);
		if ((item == null) || (item.getId() == null)) {
			return new ResultWrapper(0);
		} else {
			item = jpa.save(item);
			return new ResultWrapper(item.getId());
		}
	}

	public ResultWrapper<Integer> setNewItem(@Valid T item, BindingResult result) throws Exception {
		if (result.hasErrors() || (item == null)) {
			return new ResultWrapper(0);
		}
		item = jpa.save(item);
		return new ResultWrapper(item.getId());
	}
}
