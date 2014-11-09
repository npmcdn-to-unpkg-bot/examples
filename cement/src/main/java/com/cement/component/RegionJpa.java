package com.cement.component;

import org.springframework.stereotype.Repository;

import com.cement.model.Region;

@Repository
public class RegionJpa extends IdNamePairJpaBase<Region> {
	public RegionJpa() {
		super(Region.class);
	}
}
