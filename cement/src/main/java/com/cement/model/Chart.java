package com.cement.model;

import java.util.Collection;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "chart")
@AttributeOverrides({
	@AttributeOverride(name="id", column=@Column(name="chart_id")),
	@AttributeOverride(name="name", column=@Column(name="chart_name"))
})
public class Chart extends IdNamePair {
	@OneToMany(fetch=FetchType.LAZY, mappedBy="chart")
	@OrderBy("name")
	Collection<ChartItem> items;

	public Collection<ChartItem> getItems() {
		return items;
	}

	public void setItems(Collection<ChartItem> items) {
		this.items = items;
	}
	
}
