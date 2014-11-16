package com.cement.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cement.model.Sieve;

@Controller
@RequestMapping("/nom/sieve")
public class SieveController extends EntityWithIdControllerBase<Sieve> {
	@Autowired
	public SieveController(SieveJpa jpa) {
		super(jpa);
	}

	protected String getViewItemEdit() {
		return "IdNamePairEdit";
	}

	protected String getViewItemList() {
		return "IdNamePairList";
	}
}
