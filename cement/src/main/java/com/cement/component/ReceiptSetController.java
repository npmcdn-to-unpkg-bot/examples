package com.cement.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cement.model.CurveSieve;
import com.cement.model.Material;
import com.cement.model.ReceiptMaterial;
import com.cement.model.ReceiptSet;
import com.slavi.math.adjust.LeastSquaresAdjust;
import com.slavi.math.matrix.Matrix;

@Controller
@RequestMapping("/wizard")
public class ReceiptSetController extends IdNamePairControllerBase<ReceiptSet> {
	@Autowired
	ChartJpa chartJpa;

	ReceiptSetJpa jpa;
	
	@Autowired
	public ReceiptSetController(ReceiptSetJpa jpa) {
		super(jpa);
		this.jpa = jpa;
	}

	protected String getViewItemEdit() {
		return "ReceiptSetEdit";
	}

	protected String getViewItemList() {
		return "ReceiptSetList";
	}

	@RequestMapping(value="/{wizardId}/new", method=RequestMethod.GET)
	public String show(Model model, @PathVariable("wizardId") int wizardId) throws Exception {
		ReceiptSet wizard = jpa.load(wizardId);
		
		List<ReceiptMaterial> materials = new ArrayList<>();
		for (Material m : wizard.getMaterials()) {
			ReceiptMaterial rm = new ReceiptMaterial();
			rm.setMaterial(m);
			materials.add(rm);
		}
		
		model.addAttribute("wizard", wizard);
		model.addAttribute("labels", chartJpa.loadSieveLabels());
		return "ChartShow";
	}
}
