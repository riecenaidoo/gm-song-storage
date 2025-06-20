package com.bobo.storage.core.resource.query;

import com.bobo.semantic.TechnicalID;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface QueryRepository<T extends TechnicalID<ID>, ID> extends Repository<T, ID> {

	/**
	 * @param id {@link TechnicalID} of the {@code Resource}.
	 * @return the {@code Resource}, if it exists, otherwise {@link Optional#empty()}.
	 */
	Optional<T> findById(ID id);
}
