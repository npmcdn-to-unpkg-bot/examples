package examples.spa.backend.test;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.BaseSettings;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.BasicClassIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.SimpleMixInResolver;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.RootNameLookup;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

import examples.spa.backend.component.EntityWithIdControllerBase;
import examples.spa.backend.component.EntityWithIdJpa;
import examples.spa.backend.model.EntityWithId;

public class Dummy {

	void configureMapper(ObjectMapper m) {
		AnnotationIntrospector primary = new JacksonAnnotationIntrospector();
		AnnotationIntrospector secondary = new JaxbAnnotationIntrospector();
		AnnotationIntrospector pair = new AnnotationIntrospectorPair(primary, secondary);

		m.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		//m.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX"));
		m.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		m.setAnnotationIntrospector(pair);
		m.enable(SerializationFeature.INDENT_OUTPUT);
	}

	ObjectMapper xmlMapper() {
		ObjectMapper m = new XmlMapper();
		configureMapper(m);
		return m;
	}
	
	ObjectMapper jsonMapper() {
		ObjectMapper m = new ObjectMapper();
		configureMapper(m);
		return m;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class TestEntityWithIdBase<QQ, TT extends Serializable> implements EntityWithId<TT> {
		QQ qq;
		
		@XmlElement
		String asd;

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
		TestEntityWithIdInt val = new TestEntityWithIdInt();
		val.asd = "ASD";
		val.qq = 123;
		val.setId(222);
		
		ObjectMapper m = jsonMapper();
		System.out.println(m.writeValueAsString(val));
		System.out.println("----------");
		
		Class entType = findEntytyClass(TestControllerInt.class);
		System.out.println(entType);

		//JsonFactory _jsonFactory = new MappingJsonFactory();
		//JsonGenerator g = _jsonFactory.createGenerator(NullOutputStream.NULL_OUTPUT_STREAM);
		BaseSettings DEFAULT_BASE = new BaseSettings(
				null, // can not share global ClassIntrospector any more (2.5+)
				new JacksonAnnotationIntrospector(),
				VisibilityChecker.Std.defaultInstance(), null, TypeFactory.defaultInstance(),
				null, StdDateFormat.instance, null,
				Locale.getDefault(),
				null, // to indicate "use default TimeZone"
				Base64Variants.getDefaultVariant() // 2.1
		);
		BaseSettings base = DEFAULT_BASE.withClassIntrospector(new BasicClassIntrospector());
		SerializationConfig cfg = new SerializationConfig(base,
				new StdSubtypeResolver(), new SimpleMixInResolver(null), new RootNameLookup());
		//cfg.initialize(g);
		DefaultSerializerProvider _serializerProvider = new DefaultSerializerProvider.Impl();
		_serializerProvider = _serializerProvider.createInstance(cfg, BeanSerializerFactory.instance);
		JsonSerializer ser = _serializerProvider.findTypedValueSerializer(entType, true, null);
		
		//Object o = BeanSerializerFactory.instance.createTypeSerializer(cfg, cfg.constructType(entType));
		//ser = (JsonSerializer) o;
		
		BeanDescription bd = cfg.introspect(cfg.constructType(entType));
		List<BeanPropertyDefinition> props = bd.findProperties();
		for (BeanPropertyDefinition i : props) {
			System.out.println(i);
		}
		System.out.println("----------");
		
		for (Iterator<PropertyWriter> i = ser.properties(); i.hasNext();) {
			PropertyWriter pw = i.next();
			if (pw instanceof BeanPropertyWriter) {
				BeanPropertyWriter bpw = (BeanPropertyWriter) pw;
				System.out.println(bpw.getName() + ":" + bpw.getType() + ":" + bpw.getGenericPropertyType());
			}
		}
	}

	public static void main(String[] args) throws Exception {
		new Dummy().doIt();
		System.out.println("Done.");
	}
}
