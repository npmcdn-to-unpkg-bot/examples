package examples.spa.backend.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@Entity
@Table(name = "loc")
@AttributeOverrides({
	@AttributeOverride(name="id", column=@Column(name="loc_id")),
	@AttributeOverride(name="name", column=@Column(name="loc_name"))
})
@XmlRootElement
@JacksonXmlRootElement
public class Location extends IdNamePair {
}
