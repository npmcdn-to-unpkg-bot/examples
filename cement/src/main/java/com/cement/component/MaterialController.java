package com.cement.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/nom/location")
public class MaterialController {
	@Autowired
	MaterialJpa jpa;

	
}
