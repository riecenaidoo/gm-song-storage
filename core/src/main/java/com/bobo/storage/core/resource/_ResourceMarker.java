package com.bobo.storage.core.resource;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Sole purpose is to be referenced for annotation based scanning.
 * <p>
 * Prefaced with <code>_</code> as I want to easily distinguish this class to make sure it is not used in
 * business logic.
 *
 * @see EnableJpaRepositories#basePackageClasses()
 */
public @interface _ResourceMarker {

}
