package examples.spa.backend.myRest;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.xml.bind.annotation.XmlAttribute;

@Access(AccessType.FIELD)
public class MyRestConfigField implements Serializable {
	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public boolean searchable = false;

	@XmlAttribute
	public boolean ignoreCase = true;
	
	@XmlAttribute
	public boolean appendWildcards = true;
	
	@XmlAttribute
	public SortOrder order = SortOrder.NONE;
}
