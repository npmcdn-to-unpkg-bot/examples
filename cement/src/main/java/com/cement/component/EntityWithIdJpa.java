package com.cement.component;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.transaction.annotation.Transactional;

import com.cement.model.EntityWithId;

public abstract class EntityWithIdJpa<T extends EntityWithId> {
	@PersistenceContext
	protected EntityManager em;

	protected Class<T> entityClass;
	
	public EntityWithIdJpa(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public T makeNew() throws Exception {
		return entityClass.newInstance();
	}
	
	@Transactional(readOnly=true)
	public Collection<T> list() throws Exception {
		CriteriaQuery query = em.getCriteriaBuilder().createQuery(entityClass);
		query = query.select(query.from(entityClass));
		return em.createQuery(query).getResultList();
	}

	@Transactional(readOnly=true)
	public T load(int id) throws Exception {
		return em.find(entityClass, id);
	}

	@Transactional
	public void save(T item) throws Exception {
		if (item.getId() == null) {
			em.persist(item);
		} else {
			em.merge(item);
		}
	}
	
	@Transactional
	public void delete(int id) throws Exception {
		Object item = em.find(entityClass, id);
		em.remove(item);
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
}
