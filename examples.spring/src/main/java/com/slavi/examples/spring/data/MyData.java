package com.slavi.examples.spring.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonView;

@XmlRootElement
public class MyData {
	public interface JustId {}
	public interface WithoutBody extends JustId {};
	public interface WithBody extends WithoutBody {};
	
	@JsonView(JustId.class)
	@XmlAttribute
	public int id;
	
	@JsonView(WithoutBody.class)
	@XmlAttribute
	public String name;
	
	@JsonView(WithBody.class)
	@XmlAttribute
	public String body;
	
	public String toString() {
		return "ID: " + id + ", NAME: " + name + ", BODY: " + body;
	}
}
