package com.cement.component;

import org.springframework.stereotype.Repository;

import com.cement.model.Supplier;

@Repository
public class SupplierJpa extends IdNamePairJpaBase<Supplier> {
	public SupplierJpa() {
		super(Supplier.class);
	}
}
