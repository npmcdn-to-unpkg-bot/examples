package examples.spa.backend.component;

import examples.spa.backend.model.EntityWithId;

public abstract class IdNamePairJpaBase<T extends EntityWithId<Integer>> extends EntityWithIdJpa<Integer, T> {
	public IdNamePairJpaBase(Class<T> entityClass) {
		super(entityClass);
	}
	
	public String getOrderBy() {
		return "name";
	}
}
