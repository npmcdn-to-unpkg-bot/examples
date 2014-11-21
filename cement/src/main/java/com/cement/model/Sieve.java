package com.cement.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;

@Entity
@Table(name = "sieve")
@AttributeOverrides({
	@AttributeOverride(name="id", column=@Column(name="sieve_id"))
})
@Access(AccessType.FIELD)
@NamedNativeQueries({
	@NamedNativeQuery(name="SieveName_list", resultSetMapping="SieveName_list", 
		query=	"select s.sieve_id id, s.sieve_d name from sieve s order by s.sieve_id")
})
@SqlResultSetMappings({
	@SqlResultSetMapping(name="SieveName_list", classes={
		@ConstructorResult(targetClass=IdNamePair.class, columns = {
			@ColumnResult(name="id"),
			@ColumnResult(name="name")
		})
	})
})
public class Sieve extends EntityWithId {

	@Column(name="sieve_d", nullable=false)
	double name;

	public double getName() {
		return name;
	}

	public void setName(double name) {
		this.name = name;
	}
}
