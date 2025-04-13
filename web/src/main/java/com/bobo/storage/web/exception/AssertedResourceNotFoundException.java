package com.bobo.storage.web.exception;

import com.bobo.storage.core.domain.TechnicalID;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * The existence of a {@code Resource} was asserted in a request path,
 * but does not exist.
 * <p>
 * For the first pass I am modelling this as a checked {@code Exception}. I usually prefer to add the {@code throws}
 * declaration to my method signatures, even for {@code RuntimeExceptions}, just for documentation. I think forcing
 * the {@code throws} declaration makes sense here, because I want to know the alternate execution paths of my
 * {@code RequestMappings}.
 * <p>
 * TODO Assert is a strong word, am I {@code /playlists/1/songs} asserting that there exists a {@code Playlist} with
 *  identified by {@code 4}, or am I implying it?
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

  /**
   * TODO [design] Read more on RFC7807 Problem Details. It may be prudent to design this as a {@code ProblemDetail}
   *  object, which is modelled as a Java {@code Exception}.
   *
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc7807#section-3">RFC7807 Problem Details</a>
   */
  public String problemDetail(String requestPath) {
    return String.format("The asserted resource (%s ID:%s) in the request requestPath '%s' does not exist. " +
                                 "The validity of the target resource cannot be determined.",
                         resourceName,
                         identifier,
                         requestPath);
  }

}
