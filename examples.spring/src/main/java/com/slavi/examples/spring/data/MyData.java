package com.slavi.examples.spring.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MyData {
	@XmlAttribute
	public int id;
	@XmlAttribute
	public String name;
	@XmlAttribute
	public String body;
	
	public String toString() {
		return "ID: " + id + ", NAME: " + name + ", BODY: " + body;
	}
}
