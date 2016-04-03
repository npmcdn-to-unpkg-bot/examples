package examples.grunt.component;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import examples.grunt.model.Location;

@Configuration
public class Config {

	@Autowired
	LocationJpa jpa;
	
	@PostConstruct
	public void init() throws Exception {
		Location l = new Location();
		for (int i = 0; i < 10; i++) {
			l.setName("Name " + i);
			jpa.save(l);
		}
	}
}
