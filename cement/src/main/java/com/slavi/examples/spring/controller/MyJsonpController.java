package com.slavi.examples.spring.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

import com.fasterxml.jackson.annotation.JsonView;
import com.slavi.examples.spring.data.MyData;

@RestController
@ControllerAdvice
public class MyJsonpController extends AbstractJsonpResponseBodyAdvice {

	public MyJsonpController() {
		super("callback");
	}
	
	@RequestMapping(value="/jsonp", produces="application/json")
	@JsonView(MyData.WithoutBody.class)
	protected MyData jsonp(@RequestParam(value="q", defaultValue="<default value>") String someVal,
			@RequestBody(required=false) String body) {
		MyData r = new MyData();
		r.setId(111);
		r.setName(someVal);
		r.setBody(body);
		return r;
	}
}
