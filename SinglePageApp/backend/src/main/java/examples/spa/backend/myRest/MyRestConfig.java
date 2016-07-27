package examples.spa.backend.myRest;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.xml.bind.annotation.XmlRootElement;

import examples.spa.backend.component.UtilsService;

@XmlRootElement(name = "myRestConfig")
@Access(AccessType.FIELD)
public class MyRestConfig implements Serializable {

	MyRestConfigItem configItem[];
	
	MyRestObject rest[];

	public MyRestConfigItem[] getConfigItem() {
		return configItem;
	}

	public void setConfigItem(MyRestConfigItem[] configItem) {
		this.configItem = configItem;
	}
	
	public String toString() {
		return UtilsService.objectToString(this);
	}

	public MyRestObject[] getRest() {
		return rest;
	}

	public void setRest(MyRestObject[] rest) {
		this.rest = rest;
	}
}
