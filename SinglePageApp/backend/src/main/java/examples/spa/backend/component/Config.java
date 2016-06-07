package examples.spa.backend.component;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

import examples.spa.backend.model.Location;

@Configuration
public class Config {

	@Autowired
	LocationJpa jpa;
	
	@PostConstruct
	public void init() throws Exception {
		for (int i = 0; i < 50; i++) {
			Location l = new Location();
			l.setName("Name " + i);
			jpa.save(l);
		}
	}
	
	private void configureObjectMapper(ObjectMapper m) {
		AnnotationIntrospector i1 = new JacksonAnnotationIntrospector();
		AnnotationIntrospector i2 = new JaxbAnnotationIntrospector();
		AnnotationIntrospector pair = new AnnotationIntrospectorPair(i1, i2);
		m.setAnnotationIntrospector(pair);
	}
	
	@Bean
	public ObjectMapper xmlObjectMapper() {
		ObjectMapper m = new XmlMapper();
		configureObjectMapper(m);
		return m;
	}
	
	@Bean
	public ObjectMapper jsonObjectMapper() {
		ObjectMapper m = new ObjectMapper();
		configureObjectMapper(m);
		return m;
	}
}
