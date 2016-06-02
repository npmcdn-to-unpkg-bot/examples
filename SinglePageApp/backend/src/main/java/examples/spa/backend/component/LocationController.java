package examples.spa.backend.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import examples.spa.backend.model.Location;
import examples.spa.backend.model.ResultWrapper;

@Controller
@RequestMapping("/location")
public class LocationController extends IdNamePairControllerBase<Location> {
	@Autowired
	public LocationController(LocationJpa jpa) {
		super(jpa);
	}

	@RequestMapping(value="save3", method=RequestMethod.POST)
	@ResponseBody
	protected ResultWrapper<Integer> saveItem3(
			@RequestBody Location item) throws Exception {
		System.out.println(item);
		if ((item == null) || (item.getId() == null)) {
			return new ResultWrapper(0);
		} else {
			item = jpa.save(item);
			return new ResultWrapper(item.getId());
		}
	}

}
