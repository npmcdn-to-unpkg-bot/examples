package examples.spa.backend.component;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import examples.spa.backend.misc.FilterItemHelper;
import examples.spa.backend.model.EntityWithId;
import examples.spa.backend.model.FilterItemResponse;

public abstract class EntityWithIdJpa<ID extends Serializable, T extends EntityWithId<ID>> {
	@PersistenceContext
	protected EntityManager em;

	@Autowired
	protected UtilsService utils;

	protected Class<T> entityClass;
	
	public EntityWithIdJpa(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public T makeNew() throws Exception {
		return entityClass.newInstance();
	}
	
	@Transactional(readOnly=true)
	public FilterItemResponse filter(int page, int size, String search, String orderBy) throws Exception {
		FilterItemHelper f = utils.getFilterItemHelper(entityClass);
		return f.find(page, size, search, orderBy);
	}

	@Transactional(readOnly=true)
	public T load(ID id) {
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
}
