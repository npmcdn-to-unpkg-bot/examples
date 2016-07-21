package examples.spa.backend.misc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;

import examples.spa.backend.model.FieldDef;

public class FilterItemClassData<T> {
	
	public final Class<T> entityClass;
	
	public List<FieldDefinition> orderByFields;
	
	public Map<String, FieldDefinition> fields;
	
	public FilterItemClassData(EntityManager em, Class<T> entityClass) {
		this.entityClass = entityClass;
		initializeFieldDefinitions(em);
	}
	
	private FieldDefinition getFieldDef(String attributeName, Class fieldClass) {
		String getMethodName = "get" + attributeName;
		String isMethodName = Boolean.class.equals(fieldClass) || boolean.class.equals(fieldClass) ? "is" + attributeName : null;
		Class cl = entityClass;
		while (cl != null) {
			// First check getter methods. This is the way ot override an annotation
			for (Method m : cl.getDeclaredMethods()) {
				String name = m.getName();
				if (m.getReturnType().equals(fieldClass) &&
					(name.equalsIgnoreCase(getMethodName) || name.equalsIgnoreCase(isMethodName))) {
					FieldDef fd = m.getAnnotation(FieldDef.class);
					if (fd != null) {
						return new FieldDefinition(attributeName, fd);
					}
				}
			}

			for (Field f : cl.getDeclaredFields()) {
				if (f.getName().equalsIgnoreCase(attributeName) && f.getType().equals(fieldClass)) {
					FieldDef fd = f.getAnnotation(FieldDef.class);
					if (fd != null) {
						return new FieldDefinition(attributeName, fd);
					}
				}
			}
			cl = cl.getSuperclass();
		}
		return new FieldDefinition(attributeName);
	}
	
	protected void initializeFieldDefinitions(EntityManager em) {
		orderByFields = new ArrayList<>();
		fields = new HashMap<>();

		EntityType etype = em.getMetamodel().entity(entityClass);
		for (Attribute attr : (Set<Attribute>) etype.getAttributes()) {
			String name = attr.getName();
			String uname = name.toUpperCase();
			if (fields.containsKey(uname))
				continue;
			FieldDefinition fd = getFieldDef(name, attr.getJavaType());
			fields.put(uname, fd);
			if (fd.order != FieldDef.Order.NOT_SORTED)
				orderByFields.add(fd);
		}
		
		orderByFields.sort(new Comparator<FieldDefinition>() {
			public int compare(FieldDefinition o1, FieldDefinition o2) {
				int r = Integer.compare(o1.orderPrecednce, o2.orderPrecednce);
				if (r == 0)
					r = o1.name.compareToIgnoreCase(o2.name);
				return r;
			}
		});
	}
}
