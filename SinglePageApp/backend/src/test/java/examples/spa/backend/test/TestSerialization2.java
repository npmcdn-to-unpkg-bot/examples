package examples.spa.backend.test;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

public class TestSerialization2 {

	//@XmlJavaTypeAdapter(ContactMethodAdapter.class)
	//@MappedSuperclass
	//@XmlSeeAlso({ Address.class, PhoneNumber.class })
	@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.EXISTING_PROPERTY,
		visible = true,
		property = "ttype"
	)
	@JsonSubTypes({
		@Type(name = "address", value = Address.class),
		@Type(name = "phone", value = PhoneNumber.class)
	})
	public static abstract class ContactMethod {
		@XmlAttribute
		public String ttype;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Address extends ContactMethod {
		@XmlAttribute
		public String street;
		@XmlAttribute
		public String city;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class PhoneNumber extends ContactMethod {
		@XmlAttribute
		public String number;
	}
	
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Customer {
		@XmlElement(name = "contact-method")
		private List<ContactMethod> contactMethods;
	}
	
	private void configureObjectMapper(ObjectMapper m) {
		AnnotationIntrospector i1 = new JacksonAnnotationIntrospector();
		AnnotationIntrospector i2 = new JaxbAnnotationIntrospector();
		AnnotationIntrospector pair = new AnnotationIntrospectorPair(i1, i2);
		m.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		m.setSerializationInclusion(Include.NON_NULL);
		m.configure(SerializationFeature.INDENT_OUTPUT, true);
		m.setAnnotationIntrospector(pair);
	}
	
	public ObjectMapper xmlObjectMapper() {
		ObjectMapper m = new XmlMapper();
		configureObjectMapper(m);
		return m;
	}
	
	public ObjectMapper jsonObjectMapper() {
		ObjectMapper m = new ObjectMapper();
		configureObjectMapper(m);
		return m;
	}
	
	void doIt() throws Exception {
/*		JAXBContext jc = JAXBContext.newInstance(Customer.class);

		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Customer customer = (Customer) unmarshaller.unmarshal(getClass().getResourceAsStream("TestSerialization2.xml"));

		for (ContactMethod contactMethod : customer.contactMethods) {
			System.out.println(contactMethod.getClass());
		}

		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(customer, System.out);
	*/
		ObjectMapper mapper;
		Customer c2;
		String str;
		
		mapper = jsonObjectMapper();
		c2 = mapper.readValue(getClass().getResourceAsStream("TestSerialization2.json"), Customer.class);
		str = mapper.writeValueAsString(c2);
		System.out.println(str);
		c2 = mapper.readValue(str, Customer.class);
		
		mapper = xmlObjectMapper();
		c2 = mapper.readValue(getClass().getResourceAsStream("TestSerialization2.xml"), Customer.class);
		System.out.println(c2.contactMethods.get(0).ttype);
		str = mapper.writeValueAsString(c2);
		System.out.println(str);
		c2 = mapper.readValue(str, Customer.class);
	}

	public static void main(String[] args) throws Exception {
		new TestSerialization2().doIt();
		System.out.println("Done.");
	}
}
