package com.cement.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;

@Entity
@NamedNativeQueries({
	@NamedNativeQuery(name="SieveValue_loadPass", resultSetMapping="SieveValue_loadPass", 
		query=	"select p.sieve_id sieve_id, p.value val from material_sample_sieve_pass p " +
				"where p.sample_id=?1 and p.pass_id=?2 order by p.sieve_id"),
	@NamedNativeQuery(name="SieveName_list", resultSetMapping="SieveName_list", 
		query=	"select s.sieve_id id, s.sieve_d name from sieve s order by s.sieve_id"),
	@NamedNativeQuery(name="SieveName_list2", resultSetMapping="SieveName_list2", 
		query=	"select s.sieve_id id, s.sieve_d name from sieve s order by s.sieve_id")
})
@SqlResultSetMappings({
	@SqlResultSetMapping(name="SieveValue_loadPass", entities={ @EntityResult(entityClass=SieveValue.class) } ),
	@SqlResultSetMapping(name="SieveName_list", entities={ @EntityResult(entityClass=IdName.class) } ),
	@SqlResultSetMapping(name="SieveName_list2", columns={ 
			@ColumnResult(name="id", type=Double.class),
			@ColumnResult(name="name", type=String.class)} )
})
@Access(AccessType.FIELD)
public class SieveValue {

	@Id
	@Column(name="sieve_id")
	private int sieveId;

	@Column(name="val")
	private double val;

	public SieveValue() {
	}
	
	public SieveValue(int sieveId, double val) {
		this.sieveId = sieveId;
		this.val = val;
	}

	public int getSieveId() {
		return sieveId;
	}

	public void setSieveId(int id) {
		this.sieveId = id;
	}

	public double getVal() {
		return val;
	}

	public void setVal(double value) {
		this.val = value;
	}
	
	public String toString() {
		return "ID:" + sieveId + ", V:" + val;
	}
}
