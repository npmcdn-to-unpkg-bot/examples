package examples.grunt.component;

import org.springframework.stereotype.Repository;

import examples.grunt.model.Location;

@Repository
public class LocationJpa extends IdNamePairJpaBase<Location> {
	public LocationJpa() {
		super(Location.class);
	}
}
