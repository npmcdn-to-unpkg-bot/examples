package com.slavi.examples.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MyController {
	@RequestMapping("/helloworld")
	protected ModelAndView HelloWorld() throws Exception {
		String hello = "Hello World! My First Web App Using Spring MVC.";
		return new ModelAndView("helloworld", "hello", hello);
	}
}
