package com.bobo.storage.core.resource;

import com.bobo.semantic.TechnicalID;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/** Captures types to ensure the correct {@code id} type is used for the {@code Entity}. */
@NoRepositoryBean
public interface EntityRepository<T extends TechnicalID<ID>, ID> extends Repository<T, ID> {

	/**
	 * Find a technically identified {@code Entity}.
	 *
	 * @param id {@link TechnicalID} of the {@code Entity}.
	 * @return the {@code Entity}, if it exists, otherwise {@link Optional#empty()}.
	 */
	Optional<T> findById(ID id);
}
