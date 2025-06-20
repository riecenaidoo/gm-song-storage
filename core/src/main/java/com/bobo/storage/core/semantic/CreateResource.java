package com.bobo.storage.core.semantic;

import com.bobo.semantic.TechnicalID;

public interface CreateResource<T extends TechnicalID<?>> {

	/**
	 * Creates a {@code Resource}. It will be assigned a {@link TechnicalID}.
	 *
	 * @param resource to create; must not already have an assigned {@code id}.
	 * @return the created {@code Resource}. Equivalent to {@code resource}. Not guaranteed to be the
	 *     same {@code Object}.
	 * @throws IllegalArgumentException if {@code resource} had an {@code id} assigned.
	 */
	T create(T resource) throws IllegalArgumentException;
}
