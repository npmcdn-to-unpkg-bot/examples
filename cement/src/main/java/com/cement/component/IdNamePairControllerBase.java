package com.cement.component;

import com.cement.model.IdNamePair;

public abstract class IdNamePairControllerBase<T extends IdNamePair> extends EntityWithIdControllerBase<T> {

	public IdNamePairControllerBase(IdNamePairJpaBase<T> jpa) {
		super(jpa);
	}

	protected String getViewItemEdit() {
		return "IdNamePairEdit";
	}

	protected String getViewItemList() {
		return "IdNamePairList";
	}
}