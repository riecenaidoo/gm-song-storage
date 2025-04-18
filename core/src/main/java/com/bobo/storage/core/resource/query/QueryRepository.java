package com.bobo.storage.core.resource.query;

import com.bobo.semantic.TechnicalID;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.Optional;

@NoRepositoryBean
public interface QueryRepository<T extends TechnicalID<ID>, ID> extends Repository<T, ID> {

  /**
   * Must be overridden with a {@code default} method in JPA Repositories.
   *
   * @return the {@code Class} of {@code Resource}(s) being served through this {@link Repository}.
   */
  Class<T> resource();

  /**
   * @param id {@link  TechnicalID} of the {@code Resource}.
   * @return the {@code Resource}, if it exists, otherwise {@link Optional#empty()}.
   */
  Optional<T> findById(ID id);

  /**
   * Convenience method to retrieve a {@code Resource} that exists.
   *
   * @param id {@link  TechnicalID} of the {@code Resource}.
   * @return the asserted {@code Resource}.
   * @throws AssertedResourceNotFoundException if such a {@code Resource} did not exist.
   */
  @Deprecated
  default T get(ID id) throws AssertedResourceNotFoundException {
    return findById(id).orElseThrow(() -> new AssertedResourceNotFoundException(resource(), id));
  }

}
