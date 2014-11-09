package com.cement.component;

import org.springframework.stereotype.Repository;

import com.cement.model.MaterialSize;

@Repository
public class MaterialSizeJpa extends IdNamePairJpaBase<MaterialSize> {
	public MaterialSizeJpa() {
		super(MaterialSize.class);
	}
}
