package com.slavi.examples.spring.controller;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.slavi.examples.spring.data.MyData;

@Controller
public class MyController {
	@RequestMapping("/helloworld")
	protected ModelAndView HelloWorld() throws Exception {
		String hello = "Hello World! My First Web App Using Spring MVC.";
		return new ModelAndView("helloworld", "hello", hello);
	}
	
	@RequestMapping("/t1/{someVal}")
	protected String t1(@PathVariable("someVal") String someVal, Model model) {
		String hello = "";
		hello = "Got a param: " + someVal;
		try {
			hello = Class.forName("org.springframework.http.converter.json.MappingJackson2HttpMessageConverter").getName();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("hello", hello);
		return "helloworld";
	}
	
	@ModelAttribute("hello")
	@RequestMapping(value="/a", method = RequestMethod.POST, produces={"application/xml", "application/json", "text/html"})
	protected MyData a(@RequestParam(value="q", defaultValue="<default value>") String someVal,
			@RequestBody String body) {
		MyData r = new MyData();
		r.id = 123;
		r.name = someVal;
		r.body = body;
		System.out.println("----------");
		return r;
	}
	
	
/*	@RequestMapping(value="/asd", method=RequestMethod.POST)
	protected String asd(@ModelAttribute("myData") MyData myData, BindingResult bindingResult) {
		System.out.println("\n\nasdasdads\n\n" + myData);
		if (bindingResult.hasErrors()) {
			return "helloworld";
		} else {
			return "a";
		}
	}*/
/*	
	@RequestMapping(value="/asd", method=RequestMethod.POST)
	protected String asd(@RequestParam("myData") String myData) {
		System.out.println("\n\nasdasdads\n\n" + myData);
		return "helloworld";
	}
*/
	@RequestMapping(value="/asd", method=RequestMethod.POST)
	protected String asd(org.springframework.web.context.request.WebRequest request) {
		System.out.println("\n\nKEYS2: " + request.getParameterMap().keySet());
		return "helloworld";
	}
}
