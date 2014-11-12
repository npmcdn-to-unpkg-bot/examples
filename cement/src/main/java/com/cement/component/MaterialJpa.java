package com.cement.component;

import org.springframework.stereotype.Repository;

import com.cement.model.Material;

@Repository
public class MaterialJpa extends EntityWithIdJpa<Material> {
	public MaterialJpa() {
		super(Material.class);
	}
}
