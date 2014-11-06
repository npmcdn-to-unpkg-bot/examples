package com.slavi.cement.page;

import org.apache.click.control.AbstractContainer;

public class Div extends AbstractContainer {

	public Div(String name) {
		super(name);
	}

	public String getTag() {
		// Return the control's HTML tag.
		return "div";
	}
}
