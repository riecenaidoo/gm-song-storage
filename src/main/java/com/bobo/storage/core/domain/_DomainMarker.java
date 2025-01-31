package com.bobo.storage.core.domain;

import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * Sole purpose is to be referenced for annotation based scanning.
 * <p>
 * Prefaced with <code>_</code> as I want to easily distinguish this class to make sure it is not used in
 * business logic.
 *
 * @see EntityScan#basePackageClasses()
 */
public @interface _DomainMarker {

}
