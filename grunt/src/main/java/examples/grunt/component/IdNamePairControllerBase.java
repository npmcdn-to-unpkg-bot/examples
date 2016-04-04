package examples.grunt.component;

import examples.grunt.model.IdNamePair;

public abstract class IdNamePairControllerBase<T extends IdNamePair> extends EntityWithIdControllerBase<T> {
	public IdNamePairControllerBase(IdNamePairJpaBase<T> jpa) {
		super(jpa);
	}
}
