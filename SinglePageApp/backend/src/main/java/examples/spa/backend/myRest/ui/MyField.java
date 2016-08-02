package examples.spa.backend.myRest.ui;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@XmlAccessorType(XmlAccessType.FIELD)
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type"
	)
@JsonSubTypes({
	@Type(name = "text", value = MyFieldText.class),
	@Type(name = "combo", value = MyFieldCombo.class)
})
public abstract class MyField implements Serializable {
	public String name;
	public String label;
}
