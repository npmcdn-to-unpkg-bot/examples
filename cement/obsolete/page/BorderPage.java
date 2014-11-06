package com.slavi.cement.page;

import org.apache.click.Page;
import org.apache.log4j.Logger;

import com.slavi.util.Util;

public class BorderPage extends Page {

	protected Logger log = Logger.getLogger(getClass());
	
	public String title = getClass().getName();
	
	public String getTemplate() {
		return "/border-template.htm";
	}

	public void error(Throwable t) {
		addModel("error", Util.exceptionToString(t));
		t.printStackTrace();
	}
	
}
