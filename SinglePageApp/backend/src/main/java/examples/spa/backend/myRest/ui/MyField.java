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
	@Type(name = "label", value = MyFieldLabel.class),
	@Type(name = "text", value = MyFieldText.class),
	@Type(name = "email", value = MyFieldEmail.class),
	@Type(name = "combo", value = MyFieldCombo.class),
	@Type(name = "radio", value = MyFieldRadio.class)
})
public abstract class MyField implements Serializable {
	public String name;
	public String label;
	public int list_col_width;
	public boolean sortable;
}
