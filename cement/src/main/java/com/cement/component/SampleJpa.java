package com.cement.component;

import org.springframework.stereotype.Repository;

import com.cement.model.Sample;

@Repository
public class SampleJpa extends EntityWithIdJpa<Sample> {
	public SampleJpa() {
		super(Sample.class);
	}
}
