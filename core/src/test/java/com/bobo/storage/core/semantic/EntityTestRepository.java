package com.bobo.storage.core.semantic;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * A test accessible {@link EntityRepository}.
 *
 * <p>Used in repository slice tests that tests entities that require other entities to be present.
 * The alternative is to spin up an application context and go through the services, which is too
 * heavy-handed and is counter to what we use the slice test for.
 *
 * @param <T>
 */
@NoRepositoryBean
public interface EntityTestRepository<T extends DomainEntity> extends CrudRepository<T, Integer> {}
