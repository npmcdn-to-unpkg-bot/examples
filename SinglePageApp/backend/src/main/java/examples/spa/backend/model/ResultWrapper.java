package examples.spa.backend.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResultWrapper<T> {
	T result;

	public ResultWrapper() {}
	
	public ResultWrapper(T result) {
		this.result = result;
	}

	public T getResult() {
		return result;
	}

	public void setresult(T result) {
		this.result = result;
	}
}
