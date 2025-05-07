package com.bobo.storage.web.api.v2.controller;

import com.bobo.semantic.TechnicalID;

/**
 * Thrown when a {@code Resource} is assumed to be a {@code Subresource} of another,
 * but no such relationship exists.
 * <p>
 * Both resources exist, but they are not related as expected;
 * the assumption about their relationship is incorrect.
 */
public class SubresourceMismatchException extends RuntimeException {

  private final String resourceName;

  private final String identifier;

  private final String subResourceName;

  private final String subResourceIdentifier;

  public <R extends TechnicalID<RID>, RID,
          S extends TechnicalID<SID>, SID> SubresourceMismatchException(Class<R> resource, RID id,
                                                                        Class<S> subResource, SID subResourceId) {
    this.resourceName = resource.getSimpleName();
    this.identifier = id.toString();
    this.subResourceName = subResource.getSimpleName();
    this.subResourceIdentifier = subResourceId.toString();
  }

  public String getSubResourceIdentifier() {
    return subResourceIdentifier;
  }

  public String getSubResourceName() {
    return subResourceName;
  }

  public String getIdentifier() {
    return identifier;
  }

  public String getResourceName() {
    return resourceName;
  }

}
