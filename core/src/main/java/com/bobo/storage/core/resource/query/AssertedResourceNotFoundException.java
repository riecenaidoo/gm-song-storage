package com.bobo.storage.core.resource.query;

import com.bobo.storage.core.domain.TechnicalID;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * The existence of a {@code Resource} was asserted, but could not be found.
 * <p>
 * This is an application specific {@code Exception} that I want to handle,
 * or explicitly propagate, therefore I've modelled it as a checked {@code Exception}.
 *
 * @see Optional#orElseThrow(Supplier)
 */
public class AssertedResourceNotFoundException extends Exception {

  private final String resourceName;

  private final String identifier;

  public <T extends TechnicalID<ID>, ID> AssertedResourceNotFoundException(Class<T> resource, ID id) {
    this.resourceName = resource.getSimpleName(); // TODO [design] Resource#getName() - allow a Resource to override this.
    this.identifier = id.toString();
  }

  public String getResourceName() {
    return resourceName;
  }

  public String getIdentifier() {
    return identifier;
  }

}
