package examples.spa.backend.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import examples.spa.backend.model.Location;
import examples.spa.backend.model.FieldDef;

public class TestSearchAnnotation {

	void doIt() throws Exception {
		Location loc = new Location();
		
		Class clazz = loc.getClass();
//		System.out.println(clazz.getDeclaredField("id"));
		Class cl = clazz;
		while (cl != null) {
			for (Field f : cl.getDeclaredFields()) {
				System.out.println(f.getName() + " " + f.getAnnotation(FieldDef.class));
			}
			cl = cl.getSuperclass();
		}
		cl = clazz;
		while (cl != null) {
			for (Method m : cl.getMethods()) {
				System.out.println(m.getName() + " " + m.getAnnotation(FieldDef.class));
			}
			cl = cl.getSuperclass();
		}
	}

	public static void main(String[] args) throws Exception {
		new TestSearchAnnotation().doIt();
		System.out.println("Done.");
	}
}
