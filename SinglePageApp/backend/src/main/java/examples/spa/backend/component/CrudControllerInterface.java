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

public interface CrudControllerInterface<T extends EntityWithId> {
	@RequestMapping(value="", method=RequestMethod.GET)
	@ResponseBody
	Items<T> list() throws Exception;
	
	@RequestMapping(value="{id}", method=RequestMethod.DELETE)
	void deleteItem(@PathVariable("id") int id) throws Exception;

	@RequestMapping(value="{id}", method=RequestMethod.GET)
	@ResponseBody
	T loadItem(@PathVariable("id") int id) throws Exception;

	@RequestMapping(value="new", method=RequestMethod.GET)
	@ResponseBody
	T loadNewItem(Model model) throws Exception;

	@RequestMapping(value="{id}", method=RequestMethod.POST)
	@ResponseBody
	ResultWrapper<Integer> saveItem(@PathVariable("id") int id, @RequestBody T item) throws Exception;

	@RequestMapping(value="save", method=RequestMethod.POST)
	@ResponseBody
	ResultWrapper<Integer> saveItem2(@RequestBody T item) throws Exception;
	
	@RequestMapping(value="new", method=RequestMethod.POST)
	@ResponseBody
	ResultWrapper<Integer> setNewItem(@Valid T item, BindingResult result) throws Exception;
}
