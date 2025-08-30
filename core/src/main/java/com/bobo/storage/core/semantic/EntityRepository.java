package com.bobo.storage.core.semantic;

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
 * @implSpec The resource layer of the application.
 *     <p>This package contains classes that control access to persistent stores.
 *     <p>Method definitions should be grouped by CRUD operation. Within each group, singular
 *     operations should be defined before batch operations. Method order and parameter lists should
 *     reflect the field order of the associated entity (see {@link
 *     com.bobo.storage.core.semantic}).
 *     <p>These classes may be aware of other layers or technical concerns, but must not depend on
 *     them.
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
