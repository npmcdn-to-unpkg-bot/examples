package com.cement.component;

import org.springframework.stereotype.Repository;

import com.cement.model.Sieve;

@Repository
public class SieveJpa extends EntityWithIdJpa<Sieve> {
	public SieveJpa() {
		super(Sieve.class);
	}
}
