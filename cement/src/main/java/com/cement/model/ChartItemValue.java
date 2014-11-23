package com.cement.model;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "chart_item_value")
@Access(AccessType.FIELD)
public class ChartItemValue implements Serializable {
	@Id
	@ManyToOne
	@JoinColumn(name="item_id", nullable=false)
	ChartItem chartItem;

	@Id
	@ManyToOne
	@JoinColumn(name="sieve_id", nullable=false)
	Sieve sieve;
	
	@Column(name="value", nullable=false)
	double value;

	public ChartItem getChartItem() {
		return chartItem;
	}

	public void setChartItem(ChartItem chartItem) {
		this.chartItem = chartItem;
	}

	public Sieve getSieve() {
		return sieve;
	}

	public void setSieve(Sieve sieve) {
		this.sieve = sieve;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}
