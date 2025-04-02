package com.bobo.storage.core.resource.query;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.Optional;

@NoRepositoryBean
public interface QueryRepository<T, ID> extends Repository<T, ID> {

  Optional<T> findById(ID id);

}
