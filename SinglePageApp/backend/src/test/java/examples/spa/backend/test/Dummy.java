package examples.spa.backend.test;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collections;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

import examples.spa.backend.component.EntityWithIdControllerBase;
import examples.spa.backend.component.EntityWithIdJpa;
import examples.spa.backend.model.EntityWithId;

public class Dummy {

	public static class TestEntityWithIdBase<QQ, TT extends Serializable> implements EntityWithId<TT> {
		QQ qq;
		
		public TT getId() {
			return null;
		}

		public void setId(TT id) {
		}
	}
	
	public static class TestEntityWithIdInt extends TestEntityWithIdBase<Integer, Integer> {
		
	}
	
	public static class TestControllerBase<QQ, PP extends Serializable, SS extends TestEntityWithIdBase<QQ, PP>> extends EntityWithIdControllerBase<PP, SS> {
		public TestControllerBase(EntityWithIdJpa<PP, SS> jpa) {
			super(null);
		}
	}

	public static class TestControllerInt<RR, ZZ extends Serializable> extends TestControllerBase<RR, ZZ, TestEntityWithIdBase<RR, ZZ>> {
		public TestControllerInt(EntityWithIdJpa<Integer, TestEntityWithIdInt> jpa) {
			super(null);
		}
	}
	
	public Class getDescendand(Class clazz, Class parentClass) {
		Class p = clazz;
		while (p != null) {
			if (p.getSuperclass() == parentClass)
				return p;
			for (Class i : p.getInterfaces()) {
				if (parentClass.isAssignableFrom(i))
					return p;
			}
			p = p.getSuperclass();
		}
		return null;
	}
	
	public Class findEntytyClass(Class entityControllerClass) {
		Class entChild = EntityWithIdControllerBase.class;
		entChild = getDescendand(entityControllerClass, entChild);
		ParameterizedType type = (ParameterizedType) entChild.getGenericSuperclass();
		Type entType = type.getActualTypeArguments()[1];
		while (true) {
			if (entType instanceof Class)
				return (Class) entType;
			if (entType instanceof ParameterizedType) {
				type = (ParameterizedType) entType;
				entType = type.getRawType();
				continue;
			}
			TypeVariable[] typeParams = entChild.getTypeParameters();
			int typeIndex = typeParams.length - 1;
			while (typeIndex >= 0) {
				if (typeParams[typeIndex].getName().equals(entType.getTypeName()))
					break;
				typeIndex--;
			}
			if (typeIndex < 0)
				return null;
			entChild = getDescendand(entityControllerClass, entChild);
			if (entChild == null)
				return null;
			Type tmpType = entChild.getGenericSuperclass();
			if (!(tmpType instanceof ParameterizedType))
				return null;
			type = (ParameterizedType) tmpType;
			entType = type.getActualTypeArguments()[typeIndex];
		}
	}
	
	void doIt() throws Exception {
		Class entType = findEntytyClass(TestControllerInt.class);
		System.out.println(entType);
		
		DeserializerFactoryConfig dfc = new DeserializerFactoryConfig();
		DeserializerFactory df = new BeanDeserializerFactory(dfc);
		DefaultDeserializationContext.Impl ddc = new DefaultDeserializationContext.Impl(df);
		TypeFactory tf = TypeFactory.defaultInstance();
		JavaType jt = tf.constructType(entType);
		JsonDeserializer jd = ddc.findRootValueDeserializer(jt);
		System.out.println("-----");
		System.out.println(jd.getKnownPropertyNames());
		System.out.println("-----");
	}

	public static void main(String[] args) throws Exception {
		new Dummy().doIt();
		System.out.println("Done.");
	}
}
