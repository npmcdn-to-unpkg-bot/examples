package examples.spa.backend.myRest.ui;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class MyFieldCombo extends MyField {
	public List<String> values = new ArrayList<>();
}
