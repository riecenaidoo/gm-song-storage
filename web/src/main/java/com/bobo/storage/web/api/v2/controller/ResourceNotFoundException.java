package com.bobo.storage.web.api.v2.controller;

import com.bobo.storage.core.domain.TechnicalID;
import com.bobo.storage.core.resource.query.AssertedResourceNotFoundException;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * A {@code Resource} could not be found.
 * <p>
 * TODO Duplicated {@link AssertedResourceNotFoundException} for now, it is marked for removal. While the
 *  idea to reduce a few lines of code is noble, there is no reason to introduce that complexity. The {@link Optional}
 *  signatures are always going to be of more use, and be more declarative.
 *
 * @see Optional#orElseThrow(Supplier)
 */
public class ResourceNotFoundException extends RuntimeException {

  private final String resourceName;

  private final String identifier;

  public <T extends TechnicalID<ID>, ID> ResourceNotFoundException(Class<T> resource, ID id) {
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
