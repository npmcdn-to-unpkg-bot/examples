package com.cement.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cement.model.Curve;
import com.cement.model.CurveSieve;

@Controller
@RequestMapping("/curve")
public class CurveController extends IdNamePairControllerBase<Curve> {
	@Autowired
	ReceiptSetJpa rsjpa;

	@Autowired
	ChartJpa chartJpa;

	CurveJpa jpa;
	
	@Autowired
	public CurveController(CurveJpa jpa) {
		super(jpa);
		this.jpa = jpa;
	}
	
	protected String getViewItemList() {
		return "ChartList";
	}

	@RequestMapping(value="/{curveId}/view", method=RequestMethod.GET)
	public String show(Model model, @PathVariable("curveId") int curveId) throws Exception {
		List<CurveSieve> curve = rsjpa.loadCurveData(curveId);
		
		StringBuilder minVal = new StringBuilder();
		StringBuilder maxVal = new StringBuilder();
		StringBuilder refVal = new StringBuilder();
		String prefix = "";
		for (int index = 0; index < curve.size(); index++) {
			CurveSieve cs = curve.get(index);
			minVal.append(prefix);
			maxVal.append(prefix);
			refVal.append(prefix);
			
			minVal.append(cs.getLowValue());
			maxVal.append(cs.getUpperValue());
			refVal.append(cs.getValue());
			
			prefix = ",";
		}
		ArrayList<Object[]> datasets = new ArrayList<>();
		datasets.add(new Object[] { "min",  "#ff0000", minVal.toString() });
		datasets.add(new Object[] { "max",  "#ff0000", maxVal.toString() });
		datasets.add(new Object[] { "ref",  "#0000ff", refVal.toString() });

		//model.addAttribute("wizard", wizard);
		model.addAttribute("labels", chartJpa.loadSieveLabels());
		model.addAttribute("datasets", datasets);
		return "ChartShow";
	}
}
