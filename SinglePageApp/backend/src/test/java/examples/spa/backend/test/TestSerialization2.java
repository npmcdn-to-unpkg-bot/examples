package examples.spa.backend.test;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

public class TestSerialization2 {

	@XmlJavaTypeAdapter(ContactMethodAdapter.class)
	@XmlSeeAlso({ Address.class, PhoneNumber.class })
	public static abstract class ContactMethod {
	}
	
	@XmlJavaTypeAdapter(ContactMethodAdapter.class)
	public static class Address extends ContactMethod {
		public String street;
		public String city;
	}
	
	@XmlJavaTypeAdapter(ContactMethodAdapter.class)
	public static class PhoneNumber extends ContactMethod {
		public String number;
	}
	
	public static class ContactMethodAdapter extends XmlAdapter<AdaptedContactMethod, ContactMethod> {
		@Override
		public AdaptedContactMethod marshal(ContactMethod contactMethod) throws Exception {
			if (null == contactMethod) {
				return null;
			}
			AdaptedContactMethod adaptedContactMethod = new AdaptedContactMethod();
			if (contactMethod instanceof Address) {
				Address address = (Address) contactMethod;
				adaptedContactMethod.street = address.street;
				adaptedContactMethod.city = address.city;
			} else {
				PhoneNumber phoneNumber = (PhoneNumber) contactMethod;
				adaptedContactMethod.number = phoneNumber.number;
			}
			return adaptedContactMethod;
		}

		@Override
		public ContactMethod unmarshal(AdaptedContactMethod adaptedContactMethod) throws Exception {
			if (null == adaptedContactMethod) {
				return null;
			}
			if (null != adaptedContactMethod.number) {
				PhoneNumber phoneNumber = new PhoneNumber();
				phoneNumber.number = adaptedContactMethod.number;
				return phoneNumber;
			} else {
				Address address = new Address();
				address.street = adaptedContactMethod.street;
				address.city = adaptedContactMethod.city;
				return address;
			}
		}
	}
	
	public static class AdaptedContactMethod {
		@XmlAttribute
		public String number;

		@XmlAttribute
		public String street;

		@XmlAttribute
		public String city;
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
	
	String str = "<?xml version='1.0' encoding='UTF-8'?>\n" + 
			"<customer>\n" + 
			"    <contact-method\n" + 
			"        number='555-1111'/>\n" + 
			"    <contact-method\n" + 
			"        street='1 A St'\n" + 
			"        city = 'Any Town'/>\n" + 
			"    <contact-method\n" + 
			"        number='555-2222'/>\n" + 
			"</customer>";
	
	void doIt() throws Exception {
		JAXBContext jc = JAXBContext.newInstance(Customer.class);

		Unmarshaller unmarshaller = jc.createUnmarshaller();
		StringReader xml = new StringReader(str);
		Customer customer = (Customer) unmarshaller.unmarshal(xml);

		for (ContactMethod contactMethod : customer.contactMethods) {
			System.out.println(contactMethod.getClass());
		}

		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(customer, System.out);
		
		ObjectMapper mapper = xmlObjectMapper();
		customer = mapper.readValue(str, Customer.class);
		mapper.writeValue(System.out, customer);
	}

	public static void main(String[] args) throws Exception {
		new TestSerialization2().doIt();
		System.out.println("Done.");
	}
}
