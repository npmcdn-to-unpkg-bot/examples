package examples.grunt.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "loc")
@AttributeOverrides({
	@AttributeOverride(name="id", column=@Column(name="loc_id")),
	@AttributeOverride(name="name", column=@Column(name="loc_name"))
})
public class Location extends IdNamePair {
}
