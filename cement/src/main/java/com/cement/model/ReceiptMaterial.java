package com.cement.model;

import java.io.Serializable;

public class ReceiptMaterial implements Serializable {
	Material material;

	double minQuantity;
	double maxQuantity;
	double quantity;
	double pricePerTon;
	
	double volume;
	double price;
	
	public ReceiptMaterial() {}
	
	public ReceiptMaterial(Material m) {
		this.material = m;
	}
	
	public Material getMaterial() {
		return material;
	}
	public void setMaterial(Material material) {
		this.material = material;
	}
	public double getMinQuantity() {
		return minQuantity;
	}
	public void setMinQuantity(double minQuantity) {
		this.minQuantity = minQuantity;
	}
	public double getMaxQuantity() {
		return maxQuantity;
	}
	public void setMaxQuantity(double maxQuantity) {
		this.maxQuantity = maxQuantity;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public double getPricePerTon() {
		return pricePerTon;
	}
	public void setPricePerTon(double pricePerTon) {
		this.pricePerTon = pricePerTon;
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
}
