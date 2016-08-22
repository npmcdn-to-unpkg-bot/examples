package examples.spa.backend.myRest.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MyFormMeta implements Serializable {
	public String name;
	
	public String label;
	
	public String baseUrl;
	
	public List<String> bestRowIdColumns = new ArrayList<>();

	public List<MyField> fields = new ArrayList<>();
}
