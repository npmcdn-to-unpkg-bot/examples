package com.cement.component;

import java.util.ArrayList;
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
import com.slavi.math.MathUtil;

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

	String colors[] = {
		"#6495ed",
		"#ff6347",
		"#ffd700",
		"#00ced1",
		"#b03060",
		"#00ff7f",
		"#ffa07a",
		"#ffdead",
		"#ff3030",
		"#8b3626",
		"#ab82ff",
		"#8b7b8b",
		"#8b008b",
		"#ff7256",
		"#008b45",
		"#5cacee",
		"#eecbad",
		"#6495ed",
		"#a0522d",
		"#ffdab9",
		"#556b2f",
		"#ffec8b",
		"#2e8b57",
	};
	
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
			curVal.append(MathUtil.d2(r.get(index)));
			
			prefix = ",";
		}
		ArrayList<Object[]> datasets = new ArrayList<>();
		datasets.add(new Object[] { "min",  "#ff0000", minVal.toString() });
		datasets.add(new Object[] { "max",  "#ff0000", maxVal.toString() });
		datasets.add(new Object[] { "ref",  "#0000ff", refVal.toString() });
		datasets.add(new Object[] { "calc", "#00ffff", curVal.toString() });
		
		int dataCounter = 0;
		for (Material m : wizard.getMaterials()) {
			StringBuilder data = new StringBuilder();
			prefix = "";
			Map<Integer, Double> mdata = jpa.loadMaterialSieveData(m.getId());
			adj.processMaterialData(curve, mdata);
			for (int index = 0; index < curve.size(); index++) {
				CurveSieve cs = curve.get(index);
				Double v = mdata.get(cs.getSieve());
				double d = (v == null) ? 0.0 : v * 100;
				data.append(prefix);
				data.append(MathUtil.d2(d));
				prefix = ",";
			}
			datasets.add(new Object[] { 
				m.getMaterialType().getName() + " " + m.getMaterialSize().getName() + "; " + m.getLocation().getName() + "; " + m.getSupplier().getName(),
				colors[dataCounter], data.toString() });
			dataCounter++;
		}
		
		model.addAttribute("wizard", wizard);
		model.addAttribute("labels", chartJpa.loadSieveLabels());
		model.addAttribute("datasets", datasets);

		return "ChartShow";
	}
}
