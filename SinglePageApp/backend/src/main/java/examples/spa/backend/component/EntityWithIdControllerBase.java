package examples.spa.backend.component;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import examples.spa.backend.model.EntityWithId;
import examples.spa.backend.model.EntityWithTimestamps;
import examples.spa.backend.model.FilterItemResponse;
import examples.spa.backend.model.ResponseWrapper;

/**
 * Ideas borrowed from:
 * https://gist.github.com/wvuong/5673644
 */
// @RestController
// @RequestMapping("/someItem")
public abstract class EntityWithIdControllerBase<ID extends Serializable, T extends EntityWithId<ID>> {
	public EntityWithIdJpa<ID, T> jpa;
	
	CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.DAYS);
	
	@Autowired
	protected UtilsService utils;
	
	public EntityWithIdControllerBase(EntityWithIdJpa<ID, T> jpa) {
		this.jpa = jpa;
	}

	@RequestMapping(value="", method=RequestMethod.GET)
	public @ResponseBody FilterItemResponse filterItems(
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "20") int size,
			@RequestParam(defaultValue = "") String search,
			@RequestParam(defaultValue = "") String order,
			@RequestParam(defaultValue = "0") int draw) throws Exception {
		FilterItemResponse r = jpa.filter(page, size, search, order);
		r.draw = draw;
		return r;
	}

	@RequestMapping(value="{id}", method=RequestMethod.GET)
	public ResponseEntity<T> loadItem(@PathVariable("id") ID id) throws Exception {
		T t = jpa.load(id);
		BodyBuilder r = ResponseEntity.ok();
		if (t instanceof EntityWithTimestamps) {
			EntityWithTimestamps tt = (EntityWithTimestamps) t;
			r.cacheControl(cacheControl).lastModified(tt.getLastModified().getTime());
		}
		return r.body(t);
	}

	@RequestMapping(value="{id}", method=RequestMethod.DELETE)
	public void deleteItem(@PathVariable("id") ID id) throws Exception {
		jpa.delete(id);
	}
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public ResponseEntity saveItem(
			@RequestBody @Valid T item, BindingResult result) throws Exception {
		if (item instanceof EntityWithTimestamps) {
			ID id = item.getId();
			if (id != null) {
				EntityWithTimestamps tt = (EntityWithTimestamps) item;
				Date lastModified = tt.getLastModified();
				if (lastModified == null) {
					result.rejectValue("lastModified", "lastModified.canNotBeEmptyWhenSavingData");
				}
			}
		}
		
		if (result.hasErrors() || (item == null)) {
			return utils.makeErrorResponse(result);
		} else {
			item = jpa.save(item);
			return ResponseEntity.ok(new ResponseWrapper(item.getId()));
		}
	}

	@RequestMapping(value="new", method=RequestMethod.GET)
	public @ResponseBody T makeNewItem() throws Exception {
		return jpa.makeNew();
	}
}
