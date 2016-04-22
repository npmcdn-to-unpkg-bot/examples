package examples.spa.backend.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="list")
@XmlRootElement(name="list")
@XmlAccessorType(XmlAccessType.FIELD)
public class Items<T> {
	@XmlElement(name="item")
	List<T> privateItems;

	public Items() {}
	
	public Items(List<T> items) {
		this.privateItems = items;
	}

	public List<T> getItems() {
		return privateItems;
	}

	public void setItems(List<T> items) {
		this.privateItems = items;
	}
}
