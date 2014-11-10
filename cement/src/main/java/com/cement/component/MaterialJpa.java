package com.cement.component;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cement.model.Material;

@Repository
public class MaterialJpa {
	@PersistenceContext
	protected EntityManager em;

	public Material makeNew() throws Exception {
		return new Material();
	}
	
	@Transactional(readOnly=true)
	public Collection<Material> list() throws Exception {
		CriteriaQuery query = em.getCriteriaBuilder().createQuery(Material.class);
		query = query.select(query.from(Material.class));
		return em.createQuery(query).getResultList();
	}

	@Transactional(readOnly=true)
	public Material load(int id) throws Exception {
		return em.find(Material.class, id);
	}

	@Transactional
	public void save(Material item) throws Exception {
		if (item.getId() == null) {
			em.persist(item);
		} else {
			em.merge(item);
		}
	}
	
	@Transactional
	public void delete(int id) throws Exception {
		Object item = em.find(Material.class, id);
		em.remove(item);
	}
}
