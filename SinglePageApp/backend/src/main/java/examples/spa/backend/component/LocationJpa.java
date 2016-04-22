package examples.spa.backend.component;

import org.springframework.stereotype.Repository;

import examples.spa.backend.model.Location;

@Repository
public class LocationJpa extends IdNamePairJpaBase<Location> {
	public LocationJpa() {
		super(Location.class);
	}
}
