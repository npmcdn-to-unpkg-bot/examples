package examples.spa.backend.test;

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

import org.eclipse.persistence.descriptors.ClassExtractor;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorNode;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;
import org.eclipse.persistence.sessions.Record;
import org.eclipse.persistence.sessions.Session;

public class TestSerialization3 {
/*
	public static class ContactMethodClassExtractor extends ClassExtractor {
		public Class extractClassFromRow(Record databaseRow, Session session) {
			return "phone".equals(databaseRow.get("@ttype")) ? PhoneNumber.class : Address.class;
		}
	}
*/	
	//@XmlJavaTypeAdapter(ContactMethodAdapter.class)
	//@MappedSuperclass
	@XmlSeeAlso({ Address.class, PhoneNumber.class })
	@XmlDiscriminatorNode("ttype")
	//@XmlClassExtractor(ContactMethodClassExtractor.class)
	public static class ContactMethod {
//		@XmlAttribute
//		public String ttype;
	}
	
	//@XmlJavaTypeAdapter(ContactMethodAdapter.class)
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlDiscriminatorValue("address")
	public static class Address extends ContactMethod {
		@XmlAttribute
		public String street;
		@XmlAttribute
		public String city;
	}
	
	//@XmlJavaTypeAdapter(ContactMethodAdapter.class)
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlDiscriminatorValue("phone")
	public static class PhoneNumber extends ContactMethod {
		@XmlAttribute
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
	
	void doIt() throws Exception {
		JAXBContext jc = JAXBContext.newInstance(Customer.class);

		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Customer customer = (Customer) unmarshaller.unmarshal(getClass().getResourceAsStream("TestSerialization2.xml"));

		for (ContactMethod contactMethod : customer.contactMethods) {
			System.out.println(contactMethod.getClass());
		}

		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(customer, System.out);
	}

	public static void main(String[] args) throws Exception {
		new TestSerialization3().doIt();
		System.out.println("Done.");
	}
}
