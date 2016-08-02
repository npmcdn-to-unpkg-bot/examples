package examples.spa.backend.myRest.ui;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class MyFieldText extends MyField {
	public int minLength = 0;
	public int maxLength = Integer.MAX_VALUE;
	public boolean multiLine = false;
}
