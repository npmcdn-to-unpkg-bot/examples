package examples.grunt.component;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.transaction.annotation.Transactional;

import examples.grunt.model.EntityWithId;

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
	
	public String getOrderBy() {
		return "id";
	}
	
	@Transactional(readOnly=true)
	public Collection<T> list() throws Exception {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery query = builder.createQuery(entityClass);
		Root<T> from = query.from(entityClass);
		query.orderBy(builder.asc(from.get(getOrderBy())));
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
