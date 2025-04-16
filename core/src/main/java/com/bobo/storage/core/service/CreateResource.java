package com.bobo.storage.core.service;

import com.bobo.storage.core.domain.TechnicalID;

public interface CreateResource<T extends TechnicalID<?>> {

  /**
   * @param resource to create; must not have an assigned {@code id}.
   * @return the created {@code Resource}. Equivalent to {@code resource}. Not guaranteed to be the same {@code Object}.
   * @throws IllegalArgumentException if {@code resource} has a {@link TechnicalID} assigned.
   */
  T create(T resource) throws IllegalArgumentException;

}
