package com.bobo.storage.core.service.impl;

/**
 * Defines expectations of all service implementations.
 *
 * @param <T> type of Entity this service operates on.
 */
@Deprecated
public interface ServiceImpl<T> {

  /**
   * Retrieve the entity from the persistence context.
   * <p>
   * Deprecating this. It was a defensive coding artefact from scars gain in another project.
   * Will probably not be necessary here if we want to expose a {@code #update(T entity)} style signature.
   *
   * @param entity that may have been mutated before being passed to the service.
   * @return entity whose state reflects what is currently persisted.
   * @throws RuntimeException TODO Upgrade to an ApplicationException / EntityNotFound, etc.
   */
  @Deprecated
  T retrieve(T entity) throws RuntimeException;

}
