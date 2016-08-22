package examples.spa.backend.myRest;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.xml.bind.annotation.XmlRootElement;

import examples.spa.backend.misc.Utils;

@XmlRootElement(name = "myRestConfig")
@Access(AccessType.FIELD)
public class MyRestConfig implements Serializable {

	MyRestConfigItem configItem[] = new MyRestConfigItem[0];
	
	MyRestObject rest[] = new MyRestObject[0];

	public MyRestConfigItem[] getConfigItem() {
		return configItem;
	}

	public void setConfigItem(MyRestConfigItem[] configItem) {
		this.configItem = configItem;
	}
	
	public String toString() {
		return Utils.objectToString(this);
	}

	public MyRestObject[] getRest() {
		return rest;
	}

	public void setRest(MyRestObject[] rest) {
		this.rest = rest;
	}
}
