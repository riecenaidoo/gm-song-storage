package com.bobo.storage.core.domain;

import com.bobo.semantic.Mother;

import java.util.function.Supplier;

/**
 * A {@code Mother} of a {@link DomainEntity}.
 *
 * @see Mother
 */
public interface EntityMother<T extends DomainEntity> extends Mother<T> {

  EntityMother<T> withIds(Supplier<Integer> idSupplier);

  EntityMother<T> withIds();

  /**
   * Assign an {@code id} to the {@code Entity}.
   */
  T setId(T entity);

  /**
   * Assign an {@code id} to the {@code Entity}.
   */
  static <T extends DomainEntity> T setId(T entity, Integer id) {
    entity.setId(id);
    return entity;
  }

}
