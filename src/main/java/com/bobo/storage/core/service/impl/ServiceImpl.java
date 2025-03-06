package com.bobo.storage.core.service.impl;

/**
 * Defines expectations of all service implementations.
 *
 * @param <T> type of Entity this service operates on.
 */
public interface ServiceImpl<T> {

  /**
   * Retrieve the entity from the persistence context.
   *
   * @param entity that may have been mutated before being passed to the service.
   * @return entity whose state reflects what is currently persisted.
   * @throws RuntimeException TODO Upgrade to an ApplicationException / EntityNotFound, etc.
   */
  T retrieve(T entity) throws RuntimeException;

}
