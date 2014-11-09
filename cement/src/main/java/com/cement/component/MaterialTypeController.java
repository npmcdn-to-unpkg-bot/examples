package com.cement.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cement.model.MaterialType;

@Controller
@RequestMapping("/nom/material/type")
public class MaterialTypeController extends IdNamePairControllerBase<MaterialType> {
	@Autowired
	public MaterialTypeController(MaterialTypeJpa jpa) {
		super(jpa);
	}
}
