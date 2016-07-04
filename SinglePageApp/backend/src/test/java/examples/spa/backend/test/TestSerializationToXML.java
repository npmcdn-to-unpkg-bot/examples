package examples.spa.backend.test;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.slavi.util.StringPrintStream;

import examples.spa.backend.model.Items;
import examples.spa.backend.model.Location;
import examples.spa.backend.model.ResponseWrapper;

public class TestSerializationToXML {

	void doIt() throws Exception {
		dumpObj(0);
		ArrayList list = new ArrayList();
		list.add(1);
		list.add(new Date());
		dumpObj(list);
	}
	
	void dumpObj(Object o) throws Exception {
		ResponseWrapper obj = new ResponseWrapper(o);
		
		AnnotationIntrospector primary = new JacksonAnnotationIntrospector();
		AnnotationIntrospector secondary = new JaxbAnnotationIntrospector();
		AnnotationIntrospector pair = new AnnotationIntrospectorPair(primary, secondary);

		ObjectMapper m = Jackson2ObjectMapperBuilder.xml().build();
		//XmlMapper xm = new XmlMapper();
		//ObjectMapper m = xm;
		//m.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX"));
		m.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		m.setAnnotationIntrospector(pair);
		m.enable(SerializationFeature.INDENT_OUTPUT);
		
		StringPrintStream out = new StringPrintStream();
		m.writeValue(out, obj);

		System.out.println(out.toString());
		out = new StringPrintStream();
		//m = Jackson2ObjectMapperBuilder.json().build();
		m = new ObjectMapper();
		m.setAnnotationIntrospector(pair);
		//m.enable(SerializationFeature.INDENT_OUTPUT);
		m.writeValue(out, obj);
		System.out.println(out.toString());
	}
	
	void doIt2() throws Exception {
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
