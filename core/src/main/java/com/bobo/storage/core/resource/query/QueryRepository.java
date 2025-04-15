package com.bobo.storage.core.resource.query;

import com.bobo.storage.core.domain.TechnicalID;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.Optional;

@NoRepositoryBean
public interface QueryRepository<T extends TechnicalID<ID>, ID> extends Repository<T, ID> {

  Optional<T> findById(ID id);

}
