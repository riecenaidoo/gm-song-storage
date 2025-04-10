package com.bobo.storage.core.domain;

/**
 * Has a technical identifier.
 * <p>
 * It is used to uniquely identify an object in the system,
 * and should not be used to uniquely identify an object within the domain.
 * <p>
 * An object will define its own uniqueness, if that is required.
 * <p>
 * An object that needs to be persisted, or requires a URI should implement this interface.
 *
 * @param <T> type of the identifier.
 */
public interface TechnicalID<T> {

  /**
   * The technical identity of an object is assigned by the system.
   * It should not have a publicly accessible mutator, nor is it a parameter in any public constructor.
   *
   * @return the technical identity of this {@code Object}.
   */
  T getId();

}
