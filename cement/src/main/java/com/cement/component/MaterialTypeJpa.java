package com.cement.component;

import org.springframework.stereotype.Repository;

import com.cement.model.MaterialType;

@Repository
public class MaterialTypeJpa extends IdNamePairJpaBase<MaterialType> {
	public MaterialTypeJpa() {
		super(MaterialType.class);
	}
}
