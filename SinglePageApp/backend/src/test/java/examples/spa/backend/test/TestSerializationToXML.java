package examples.spa.backend.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.slavi.util.StringPrintStream;

import examples.spa.backend.model.Items;
import examples.spa.backend.model.Location;
import examples.spa.backend.model.ResponseWrapper;
import examples.spa.backend.myRest.ui.MyField;
import examples.spa.backend.myRest.ui.MyFieldText;
import examples.spa.backend.myRest.ui.MyFormMeta;

public class TestSerializationToXML {

	void doIt() throws Exception {
		MyFieldText text = new MyFieldText();
		text.label = "my label";
		text.name = "my name";
		text.multiLine = true;
		
		MyFormMeta form = new MyFormMeta();
		form.fields.add(text); //new MyField[] { text };
		form.bestRowIdColumns = new ArrayList<>();
		form.bestRowIdColumns.add("asd");
		form.bestRowIdColumns.add("qwe");
		
		ObjectMapper m = jsonMapper();
		StringPrintStream out = new StringPrintStream();
		m.writeValue(out, form);
		String str = out.toString();
		System.out.println(str);
		
		MyFormMeta f = m.readValue(str, MyFormMeta.class);
		m.writeValue(System.out, f);
		
		System.out.println(str);
	}
	
	void doIt2() throws Exception {
		dumpObj(0);
		ArrayList list = new ArrayList();
		list.add(1);
		list.add(new Date());
		dumpObj(list);
		
		list.clear();
		Map map = new HashMap();
		map.put("a", "AAA");
		map.put("b", "BBB");
		map.put("c", "CCC");
		map.put("d", "DDD");

		Map map2 = new HashMap();
		map2.put("a2", "AAA-2");
		map2.put("b2", "BBB-2");
		map2.put("c2", "CCC-2");
		map2.put("d2", "DDD-2");
		
		map.put("map", map2);
		
		list.add(map2);
		list.add(map);
		dumpObj(list);
	}
	
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
	
	
	void dumpObj(Object o) throws Exception {
		Object obj = new ResponseWrapper(o);
		
		StringPrintStream out = new StringPrintStream();
		xmlMapper().writeValue(out, obj);

		System.out.println(out.toString());
		out = new StringPrintStream();
		jsonMapper().writeValue(out, obj);
		System.out.println(out.toString());
	}
	
	void doIt3() throws Exception {
		ArrayList list = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			Location l = new Location();
			l.setName("Name " + i);
			list.add(l);
		}
		Items items = new Items(list);

		
		AnnotationIntrospector primary = new JacksonAnnotationIntrospector();
		AnnotationIntrospector secondary = new JaxbAnnotationIntrospector();
		AnnotationIntrospector pair = new AnnotationIntrospectorPair(primary, secondary);

		//ObjectMapper m = Jackson2ObjectMapperBuilder.xml().build();
		XmlMapper xm = new XmlMapper();
		ObjectMapper m = xm;
		m.setAnnotationIntrospector(pair);
		m.enable(SerializationFeature.INDENT_OUTPUT);
		
		StringPrintStream out = new StringPrintStream();
		m.writeValue(out, items);

		System.out.println(out.toString());
		out = new StringPrintStream();
		//m = Jackson2ObjectMapperBuilder.json().build();
		m = new ObjectMapper();
		m.setAnnotationIntrospector(pair);
		//m.enable(SerializationFeature.INDENT_OUTPUT);
		m.writeValue(out, items);
		System.out.println(out.toString());

		out = new StringPrintStream();
		m = new ObjectMapper();
		m.setAnnotationIntrospector(pair);
		//m.enable(SerializationFeature.INDENT_OUTPUT);
		JsonGenerator generator = m.getFactory().createGenerator(out, JsonEncoding.UTF8);
		m.writeValue(generator, items);
		System.out.println(out.toString());
	}

	public static void main(String[] args) throws Exception {
		new TestSerializationToXML().doIt();
		System.out.println("Done.");
	}
}
