package com.cement.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cement.model.MaterialSize;

@Controller
@RequestMapping("/nom/material/size")
public class MaterialSizeController extends IdNamePairControllerBase<MaterialSize> {
	@Autowired
	public MaterialSizeController(MaterialSizeJpa jpa) {
		super(jpa);
	}
}
