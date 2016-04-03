package examples.grunt.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import examples.grunt.model.Location;

@Controller
@RequestMapping("/location")
public class LocationController extends IdNamePairControllerBase<Location> {
	@Autowired
	public LocationController(LocationJpa jpa) {
		super(jpa);
	}
}
