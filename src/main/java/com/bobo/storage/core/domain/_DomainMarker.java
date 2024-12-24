package com.bobo.storage.core.domain;

import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * Prefaced with <code>_</code> as I want to easily distinguish this class to make sure it is not used in
 * business logic. Its purpose is simply for annotation base scanning.
 *
 * @see EntityScan#basePackageClasses()
 */
public @interface _DomainMarker {

}
