package examples.spa.backend.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

@XmlRootElement(name = "object")
@Access(AccessType.FIELD)
public class ResponseWrapper<T> {
	T response;

	public ResponseWrapper() {}
	
	public ResponseWrapper(T response) {
		this.response = response;
	}

	public T getResponse() {
		return response;
	}

	public void setResponse(T response) {
		this.response = response;
	}
}
