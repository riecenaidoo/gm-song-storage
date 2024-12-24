package com.bobo.storage.core.resource;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Prefaced with <code>_</code> as I want to easily distinguish this class to make sure it is not used in
 * business logic. Its purpose is simply for annotation base scanning.
 *
 * @see EnableJpaRepositories#basePackageClasses()
 */
public @interface _ResourceMarker {

}
