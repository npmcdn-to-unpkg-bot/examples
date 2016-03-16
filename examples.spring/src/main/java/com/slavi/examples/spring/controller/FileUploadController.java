package com.slavi.examples.spring.controller;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.slavi.util.Util;

@SessionAttributes("status")
@Controller
public class FileUploadController {

	public static class MyException extends Exception {
	}
	
	
	@RequestMapping(value = "/status")
	public String status(Model model, @RequestParam(value="status", required=false) String status) {
		if (StringUtils.trimToNull(status) != null)
			model.addAttribute("status", status);
		else if (!model.containsAttribute("status"))
			model.addAttribute("status", "");
		return "status";
	}
	
	@RequestMapping(value = "/www")
	public String www(Model model, RedirectAttributes redir) {
		model.addAttribute("status", "Some status");
		return "redirect:status";
	}
	
	@RequestMapping(value = "/throwException")
	public String throwException(Model model, RedirectAttributes redir) throws Exception {
		if (model != null) 
			throw new MyException();
		return "redirect:status";
	}
	
	@ExceptionHandler(MyException.class)
	public ModelAndView handleIOException(MyException ex) {
		ModelAndView result = new ModelAndView("status");
		result.addObject("status", Util.exceptionToString(ex));
		return result;
	}	
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String handleFormUpload(Model model, 
			@RequestParam(value="name", required=false) String name,
			@RequestParam(value="file", required=false) MultipartFile file) throws IOException {
		if (!file.isEmpty()) {
			byte[] bytes = file.getBytes();
			model.addAttribute("status", "Uploaded " + Integer.toString(bytes.length) + " bytes.");
			// store the bytes somewhere
			return "redirect:status";
		}
		model.addAttribute("status", "Uploading FAILED.");
		return "redirect:status";
	}
}