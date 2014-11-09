package com.cement.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cement.model.Location;

@Controller
@RequestMapping("/nom/location")
public class LocationController extends IdNamePairControllerBase<Location> {
	@Autowired
	public LocationController(LocationJpa jpa) {
		super(jpa);
	}
}
