package examples.spa.backend.component;

import examples.spa.backend.model.IdNamePair;

public abstract class IdNamePairJpaBase<T extends IdNamePair> extends EntityWithIdJpa<T> {
	public IdNamePairJpaBase(Class<T> entityClass) {
		super(entityClass);
	}
	
	public String getOrderBy() {
		return "name";
	}
}
