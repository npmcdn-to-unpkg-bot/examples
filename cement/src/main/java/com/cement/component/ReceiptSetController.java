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

import com.cement.misc.Adjust;
import com.cement.model.CurveSieve;
import com.cement.model.Material;
import com.cement.model.ReceiptMaterial;
import com.cement.model.ReceiptSet;
import com.cement.model.Sieve;
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
		List<CurveSieve> curve = jpa.loadCurveData(1);
		
		List<ReceiptMaterial> materials = new ArrayList<>();
		for (Material m : wizard.getMaterials()) {
			ReceiptMaterial rm = new ReceiptMaterial();
			rm.setMaterial(m);
			materials.add(rm);
		}
		
		List<ReceiptMaterial> rm = new ArrayList<>();
		for (Material m : wizard.getMaterials()) {
			rm.add(new ReceiptMaterial(m));
		}
		
		Adjust adj = new Adjust(jpa);
		List<Double> r = adj.calc(curve, rm, 1, 1000.0);
		
		StringBuilder minVal = new StringBuilder();
		StringBuilder maxVal = new StringBuilder();
		StringBuilder refVal = new StringBuilder();
		StringBuilder curVal = new StringBuilder();
		String prefix = "";
		for (int index = 0; index < curve.size(); index++) {
			CurveSieve cs = curve.get(index);
			minVal.append(prefix);
			maxVal.append(prefix);
			refVal.append(prefix);
			curVal.append(prefix);
			
			minVal.append(cs.getLowValue());
			maxVal.append(cs.getUpperValue());
			refVal.append(cs.getValue());
			curVal.append(r.get(index));
			
			prefix = ",";
		}
		ArrayList<Object[]> datasets = new ArrayList<>();
		datasets.add(new Object[] { "min",  "#ff0000", minVal.toString() });
		datasets.add(new Object[] { "max",  "#ff0000", maxVal.toString() });
		datasets.add(new Object[] { "ref",  "#0000ff", refVal.toString() });
		datasets.add(new Object[] { "calc", "#00ffff", curVal.toString() });
		
		model.addAttribute("wizard", wizard);
		model.addAttribute("labels", chartJpa.loadSieveLabels());
		model.addAttribute("datasets", datasets);

		return "ChartShow";
	}
}
