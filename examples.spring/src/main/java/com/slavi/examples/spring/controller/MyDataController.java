package com.slavi.examples.spring.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.slavi.examples.spring.data.MyData;

@Controller
@RequestMapping("/myData")
public class MyDataController {
	static final String messageName = "myData.message";
	
	Map<Integer, MyData> data = new HashMap<Integer, MyData>();
	
	@RequestMapping(method=RequestMethod.GET)
	protected String list(Model model, RedirectAttributes redir) {
		ArrayList<MyData> r = new ArrayList<>();
		r.add(new MyData(1, "Name 1"));
		r.add(new MyData(2, "Name 2"));
		r.add(new MyData(3, "Name 3"));
		model.addAttribute("myDataList", r);
		return "myDataList";
	}
	
	@RequestMapping(value="{id}", method=RequestMethod.POST)
	protected String setMyData(Model model, RedirectAttributes redir,
			@PathVariable("id") Integer id,
			@ModelAttribute("myData") MyData myData) {
		if ((myData == null) || 
			(id != null && (!id.equals(myData.getId()))) || 
			(id == null && myData.getId() != null)) {
			model.addAttribute(messageName, "Oooops");
			return "redirect:new";
		}
		data.put(myData.getId(), myData);
		String msg = "Saved some data " + (new Date()).toString();
		redir.addFlashAttribute(messageName, msg);
		System.out.println("--- set session attribute to " + msg);
		return "redirect:{id}";
	}
	
	@RequestMapping(value="{id}", method=RequestMethod.GET)
	protected String getMyData(ModelMap model,
			@PathVariable("id") Integer id) {
		MyData r = data.get(id);
		if (r == null) {
			return "redirect:new";
		}
		String message = (String) model.get(messageName);
		System.out.println("--- message: " + message);
		model.addAttribute("hello", message == null ? "no message" : message);
		model.addAttribute("myData", r);
		return "myData";
	}

	@RequestMapping(value="new", method=RequestMethod.GET)
	protected String getNewMyData(Model model) {
		MyData r = new MyData();
		r.setId(data.size() + 1);
		r.setName("Name of myData with id of " + r.getId());
		r.setBody("Some body");
		model.addAttribute("hello", "Creating new");
		model.addAttribute("myData", r);
		return "myData";
	}

	@RequestMapping(value="new", method=RequestMethod.POST)
	protected String setNewMyData(Model model,
			@ModelAttribute("myData") MyData myData) {
		return "forward:" + myData.getId();
	}
}
