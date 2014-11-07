package com.cement.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
public class CementJpa {
	@PersistenceContext
	private EntityManager em;
}
