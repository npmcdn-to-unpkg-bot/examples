package com.slavi.examples.spring.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
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
	@RequestMapping(value="/pdf", produces="application/pdf")
	protected MyData pdf(@RequestParam(value="q", defaultValue="<default value>") String someVal,
			@RequestBody(required=false) String body) {
		MyData r = new MyData();
		r.id = 123;
		r.name = someVal;
		r.body = body;
		return r;
	}
	
	@ModelAttribute("hello")
	@RequestMapping(value="/a", produces={"application/xml", "application/json", "text/html", "application/pdf"})
	protected MyData a(@RequestParam(value="q", defaultValue="<default value>") String someVal,
			@RequestBody(required=false) String body) {
		MyData r = new MyData();
		r.id = 123;
		r.name = someVal;
		r.body = body;
		return r;
	}
	
	@RequestMapping(value="/cookie")
	protected String cookie(Model model, @CookieValue(value="myCookie", defaultValue="default cookie") String myCookie, HttpServletResponse response) {
		model.addAttribute("hello", myCookie);
		response.addCookie(new Cookie("myCookie", myCookie + " 1"));
		System.out.println("cookie: " + myCookie);
		return "a";
	}
	
	@RequestMapping(value="/asd", method=RequestMethod.POST)
	protected String asd(Model model, @ModelAttribute("myData") MyData myData, BindingResult bindingResult) {
		model.addAttribute("hello", myData);
		System.out.println("myData: " + myData);
		if (bindingResult.hasErrors()) {
			return "helloworld";
		} else {
			return "a";
		}
	}
/*	
	@RequestMapping(value="/asd", method=RequestMethod.POST)
	protected String asd(@RequestParam("myData") String myData) {
		System.out.println("\n\nasdasdads\n\n" + myData);
		return "helloworld";
	}
*/
/*
	@RequestMapping(value="/asd", method={RequestMethod.GET, RequestMethod.POST})
	protected String asd(Model model, HttpServletRequest request) {
		String str = "KEYS2: ";
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			str += names.nextElement();
			str += ",";
		}
		model.addAttribute("hello", str);
		System.out.println(str);
		return "helloworld";
	}
	*/
}
