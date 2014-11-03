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
	Integer id;

	@JsonView(WithoutBody.class)
	@XmlAttribute
	String name;
	
	@JsonView(WithBody.class)
	@XmlAttribute
	String body;
	
	public MyData() {
	}
	
	public MyData(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String toString() {
		return "ID: " + id + ", NAME: " + name + ", BODY: " + body;
	}
}
