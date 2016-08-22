package examples.spa.backend.myRest.ui;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class MyFieldEmail extends MyField {
	public int maxLength = Integer.MAX_VALUE;
	public boolean required;
}
