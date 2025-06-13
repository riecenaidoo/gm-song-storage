package com.bobo.storage.core.domain;

import com.bobo.semantic.TechnicalID;
import jakarta.persistence.*;

/**
 * Represents an entity within the domain model.
 * <p>
 * {@code DomainEntities} are expected to enforce their domain invariants and expose meaningful behavior,
 * ensuring the integrity and consistency of the domain model.
 */
@MappedSuperclass
public abstract class DomainEntity implements TechnicalID<Integer> {

  /**
   * Technical identifier of this entity.
   * <p>
   * See JSR 338: Java Persistence API (JPA) Specification (v2.2), section 2.3.1 "Default Access Type":
   * <blockquote>
   * The default access type of an entity hierarchy is determined by the placement of mapping annotations
   * on the attributes of the entity classes.
   * </blockquote>
   * <p>
   * By placing the {@link Id} annotation on this field, all implementing entities inherit field-based access
   * ({@link AccessType#FIELD}). This enforces a consistent access strategy across the hierarchy, as mixing
   * field and property access is not allowed.
   * <p>
   * Alternatively, access could be explicitly configured using {@link Access}.
   *
   * @see TechnicalID
   */
  @Id
  @GeneratedValue
  private Integer id;

  /**
   * Base constructor for {@code DomainEntity}.
   * <p>
   * See JPA 2.2, section 2.1 "The Entity Class":
   * <blockquote>
   * The entity class must have a no-arg constructor. The entity class may have other constructors as well.
   * The no-arg constructor must be public or protected.
   * </blockquote>
   * <p>
   * A {@code protected} no-args constructor is required for all {@code DomainEntity} implementations.
   * <p>
   * All {@code DomainEntity} instances must be valid upon construction via a {@code public} constructor,
   * and are responsible for maintaining their invariants during mutation.
   */
  protected DomainEntity() {
  }

  /**
   * The {@link TechnicalID} of a {@link DomainEntity} is managed exclusively by the persistence provider.
   * <p>
   * This mutator exists solely for internal use in testing scenarios.
   *
   * @param id the technical identifier to assign to this {@link Entity}
   * @see TechnicalID#getId()
   */
  protected void setId(Integer id) {
    this.id = id;
  }

  @Override
  public Integer getId() {
    return id;
  }

}
