package examples.spa.backend.component;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import examples.spa.backend.model.Location;
import examples.spa.backend.validation.LocationValidator;

@Controller
@RequestMapping("/locations")
public class LocationController extends IdNamePairControllerBase<Location> {
	
	CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.DAYS);
	
	@Autowired
	public LocationController(LocationJpa jpa) {
		super(jpa);
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new LocationValidator());
	}
}
