package com.cement.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "chart_item")
@AttributeOverrides({
	@AttributeOverride(name="id", column=@Column(name="item_id")),
	@AttributeOverride(name="name", column=@Column(name="item_name"))
})
@Access(AccessType.FIELD)
public class ChartItem extends IdNamePair {

	@ManyToOne
	@JoinColumn(name="chart_id", nullable=false)
	Chart chart;
	
	@Column(name="item_color")
	String color;

	public Chart getChart() {
		return chart;
	}

	public void setChart(Chart chart) {
		this.chart = chart;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
