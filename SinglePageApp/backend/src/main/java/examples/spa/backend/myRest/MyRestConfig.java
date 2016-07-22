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

	public MyRestConfigItem[] getConfigItem() {
		return configItem;
	}

	public void setConfigItem(MyRestConfigItem[] configItem) {
		this.configItem = configItem;
	}
	
	public String toString() {
		return UtilsService.objectToString(this);
	}
}
