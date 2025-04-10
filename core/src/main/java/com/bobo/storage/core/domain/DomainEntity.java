package com.bobo.storage.core.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class DomainEntity implements TechnicalID<Integer> {

  /**
   * JSR 338: Java Persistence API (JPA) Specification (v.2.2):
   * <p>
   * 2.3.1 Default Access Type:
   * <p>
   * "The default access type of an entity hierarchy is determined by the placement of mapping annotations on the
   * attributes of the entity classes."
   * <p>
   * Anything that implements this contract will inherit an {@link Id} mapping via its {@link #getId()} property,
   * and be defaulted to @AccessType.PROPERTY (as opposed to FIELD).
   * <p>
   * We could manually configure this using @Access.
   */
  private Integer id;

  /**
   * A {@code DomainEntity} must be valid upon construction via a {@code public} constructor.
   * It is responsible for maintaining its invariants, if any, upon mutation.
   * <p>
   * All {@code DomainEntities} must implement a {@code protected} no-args constructor.
   * <p>
   * JSR 338: Java Persistence API (JPA) Specification (v.2.2):
   * <p>
   * 2.1 The Entity Class:
   * "The entity class must have a no-arg constructor.
   * The entity class may have other constructors as well.
   * The no-arg constructor must be public or protected."
   */
  protected DomainEntity() {

  }

  /**
   * This property (mutator) will be used dynamically by the JPA provider to populate this object after construction via
   * the no-args constructor.
   *
   * @param id to assign to this {@link Entity}.
   */
  protected void setId(Integer id) {
    this.id = id;
  }

  @Id
  @GeneratedValue
  @Override
  public Integer getId() {
    return id;
  }

}
