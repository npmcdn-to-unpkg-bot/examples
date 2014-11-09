package com.cement.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cement.model.Region;

@Controller
@RequestMapping("/nom/region")
public class RegionController extends IdNamePairControllerBase<Region> {
	@Autowired
	public RegionController(RegionJpa jpa) {
		super(jpa);
	}
}
