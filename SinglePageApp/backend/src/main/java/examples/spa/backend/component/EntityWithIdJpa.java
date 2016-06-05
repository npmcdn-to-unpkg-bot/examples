package examples.spa.backend.component;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.transaction.annotation.Transactional;

import examples.spa.backend.model.EntityWithId;

public abstract class EntityWithIdJpa<ID extends Serializable, T extends EntityWithId<ID>> {
	@PersistenceContext
	protected EntityManager em;

	protected Class<T> entityClass;
	
	public EntityWithIdJpa(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public T makeNew() throws Exception {
		return entityClass.newInstance();
	}
	
	public String getOrderBy() {
		return "id";
	}
	
	@Transactional(readOnly=true)
	public List<T> list(int page, int size) throws Exception {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery crQuery = builder.createQuery(entityClass);
		Root<T> from = crQuery.from(entityClass);
		crQuery.orderBy(builder.asc(from.get(getOrderBy())));
		TypedQuery<T> query = em.createQuery(crQuery);
		query.setFirstResult(page * size);
		query.setMaxResults(size);
		return query.getResultList();
	}

	@Transactional(readOnly=true)
	public T load(ID id) throws Exception {
		return em.find(entityClass, id);
	}

	@Transactional
	public T save(T item) throws Exception {
		if (item.getId() == null) {
			em.persist(item);
			return item;
		} else {
			return em.merge(item);
		}
	}
	
	@Transactional
	public void delete(ID id) throws Exception {
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
