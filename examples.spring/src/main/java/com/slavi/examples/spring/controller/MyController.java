package com.slavi.examples.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MyController {
	@RequestMapping("/helloworld")
	protected ModelAndView HelloWorld() throws Exception {
		String hello = "Hello World! My First Web App Using Spring MVC.";
		return new ModelAndView("helloworld", "hello", hello);
	}
	
	@RequestMapping("/t1/{someVal}")
	protected String t1(@PathVariable("someVal") String someVal, Model model) {
		model.addAttribute("hello", "Got a param: " + someVal);
		return "helloworld";
	}
}
