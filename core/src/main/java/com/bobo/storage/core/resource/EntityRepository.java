package com.bobo.storage.core.resource;

import com.bobo.semantic.TechnicalID;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/**
 * Represents a collection of entities.
 *
 * @apiNote Instances of this repository should be treated as collective nouns representing their
 *     entity type. e.g.
 *     <pre>
 *   {@code private final AppleRepository apples;}
 * </pre>
 *
 * @implNote Fundamentally, this interface is serving the same purpose as {@link Repository}, but
 *     for our system.
 */
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
