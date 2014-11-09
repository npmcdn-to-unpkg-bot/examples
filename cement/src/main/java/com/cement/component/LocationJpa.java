package com.cement.component;

import org.springframework.stereotype.Repository;

import com.cement.model.Location;

@Repository
public class LocationJpa extends IdNamePairJpaBase<Location> {
	public LocationJpa() {
		super(Location.class);
	}
}
