package examples.spa.backend.test;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.propertyeditors.ClassEditor;

import examples.spa.backend.model.Location;

public class TestBeanWrapper {

	void doIt() throws Exception {
		BeanWrapper b = new BeanWrapperImpl(Location.class);
		Map<String, Object> map = new HashMap<>();
		map.put("id", 1.923);
		map.put("name", 321.123);
		b.setPropertyValues(map);
		System.out.println(b.getWrappedInstance());

		ClassEditor ce = new ClassEditor();
		ce.setValue((new Location[0]).getClass());
		String asText = ce.getAsText();
		System.out.println(asText);
		
		ce = new ClassEditor();
		ce.setAsText(asText);
		System.out.println(ce.getCustomEditor());
	}

	public static void main(String[] args) throws Exception {
		new TestBeanWrapper().doIt();
		System.out.println("Done.");
	}
}
