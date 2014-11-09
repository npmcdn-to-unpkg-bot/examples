package com.cement.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cement.model.Supplier;

@Controller
@RequestMapping("/nom/supplier")
public class SupplierController extends IdNamePairControllerBase<Supplier> {
	@Autowired
	public SupplierController(SupplierJpa jpa) {
		super(jpa);
	}
}
