package com.slavi.examples.spring.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.slavi.examples.spring.data.MyData;

@Controller
@RequestMapping("/myData")
public class MyDataController {

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
//		dataBinder.setDisallowedFields("id");
	}

	Map<Integer, MyData> data = new ConcurrentHashMap<Integer, MyData>();
	
	@RequestMapping(value="new", method=RequestMethod.GET)
	protected String makeItem(Model model) {
		MyData r = new MyData();
		model.addAttribute("myData", r);
		model.addAttribute("hello", "Invoked add");
		return "myData";
	}

	@RequestMapping(value="new", method=RequestMethod.POST)
	protected String addNewItem(Model model, RedirectAttributes redir,
			@ModelAttribute("myData") MyData myData) {
		data.put(myData.getId(), myData);
		model.addAttribute("myData", myData);
		model.addAttribute("hello", "Invoked add");
		return "redirect:" + myData.getId();
	}
	
	@RequestMapping(value="{myDataId}", method=RequestMethod.GET)
	protected String getMyData(Model model, RedirectAttributes redir,
			@PathVariable("myDataId") Integer myDataId,
			@RequestBody(required=false) String body) {
		MyData r = data.get(myDataId);
		if (r == null)
			return "redirect:new";
		model.addAttribute("myData", r);
		model.addAttribute("hello", "Invoked getMyData");
		return "myData";
	}
	
	@RequestMapping(value="{myDataId}", method=RequestMethod.POST)
	protected String setMyData(Model model, RedirectAttributes redir,
			@ModelAttribute("myData") MyData myData) {
		myData.setId(myData.getId() + 1);
		model.addAttribute("myData", myData);
		model.addAttribute("hello", "Saved some data");
		return "redirect:new";
	}

}
