package examples.spa.backend.myRest.ui;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MyForm implements Serializable {
	public String name;
	
	public MyField field;
}
