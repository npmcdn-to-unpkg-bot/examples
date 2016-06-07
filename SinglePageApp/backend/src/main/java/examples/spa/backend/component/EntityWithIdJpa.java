package examples.spa.backend.component;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import examples.spa.backend.misc.FilterItemHelper;
import examples.spa.backend.model.EntityWithId;
import examples.spa.backend.model.FilterItemResponse;

public abstract class EntityWithIdJpa<ID extends Serializable, T extends EntityWithId<ID>> {
	public static final int maxRecordsetRecords = 100;
	public static final int maxAjaxRecordsetRecords = 20;

	@PersistenceContext
	protected EntityManager em;

	protected Class<T> entityClass;
	
	protected FilterItemHelper<T> filterItemHelper;
	
	public EntityWithIdJpa(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public T makeNew() throws Exception {
		return entityClass.newInstance();
	}
	
	protected FilterItemHelper<T> getFilterItemHelper() {
		FilterItemHelper<T> r = filterItemHelper;
		if (r == null) {
			synchronized (this) {
				r = filterItemHelper;
				if (r == null) {
					this.filterItemHelper = new FilterItemHelper<>(em, entityClass);
					r = filterItemHelper;
				}
			}
		}
		return r;
	}

	@Transactional(readOnly=true)
	public FilterItemResponse<T> list(int page, int size, String search, String orderBy) throws Exception {
		FilterItemHelper<T> f = getFilterItemHelper();
		return f.find(page, size, search, orderBy);
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
