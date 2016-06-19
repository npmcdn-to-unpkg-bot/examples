package examples.spa.backend.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "object")
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
