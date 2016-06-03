package examples.spa.backend.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderBy;

@MappedSuperclass
@Access(AccessType.FIELD)
public class IdNamePair implements EntityWithId<Integer> {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(nullable = false, insertable = false, updatable = false)
	Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(nullable=false)
	@OrderBy
	String name;

	public IdNamePair() {
	}
	
	public IdNamePair(Integer id, String name) {
		setId(id);
		this.name = name;
	}

	public IdNamePair(Integer id, double name) {
		setId(id);
		this.name = Double.toString(name);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return getClass().getSimpleName() + " [id:" + getId() + ", name:" + getName() + "]";
	}
}
