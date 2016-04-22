package examples.spa.backend.component;

import examples.spa.backend.model.IdNamePair;

public abstract class IdNamePairControllerBase<T extends IdNamePair> extends EntityWithIdControllerBase<T> {
	public IdNamePairControllerBase(IdNamePairJpaBase<T> jpa) {
		super(jpa);
	}
}
