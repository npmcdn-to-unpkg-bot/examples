package com.cement.component;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cement.model.Chart;
import com.cement.model.ChartItem;
import com.cement.model.ChartItemValue;

@Controller
@RequestMapping("/chart")
public class ChartController extends IdNamePairControllerBase<Chart> {
	ChartJpa jpa;
	
	@Autowired
	public ChartController(ChartJpa jpa) {
		super(jpa);
		this.jpa = jpa;
	}
	
	protected String getViewItemList() {
		return "ChartList";
	}

	@RequestMapping(value="/{chartId}/view", method=RequestMethod.GET)
	public String show(Model model, @PathVariable("chartId") int chartId) throws Exception {
		Chart chart = jpa.load(chartId);
		
		ArrayList<Object[]> datasets = new ArrayList<>();
		for (ChartItem ci : chart.getItems()) {
			StringBuilder data = new StringBuilder();
			String prefix = "";
			for (Object v : jpa.loadChartItemValues(ci.getId())) {
				data.append(prefix);
				data.append(v);
				prefix = ",";
			}
			datasets.add(new Object[] {
				ci.getName(),
				ci.getColor(),
				data.toString()
			});
		}
		model.addAttribute("chart", chart);
		model.addAttribute("labels", jpa.loadSieveLabels());
		model.addAttribute("datasets", datasets);
		return "ChartShow";
	}
}
