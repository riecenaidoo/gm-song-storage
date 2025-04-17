package com.bobo.storage.web.api.v1.controller;

import com.bobo.storage.core.resource.query.AssertedResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * "Our goal is to not handle exceptions explicitly in Controller methods where possible.
 * They are a cross-cutting concern better handled separately in dedicated code."
 *
 * @see <a href="https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc">Exception Handling in Spring MVC</a>
 */
@ControllerAdvice
public class ControllerExceptionHandler {

  /**
   * The existence of a {@code Resource} was implied in a request path, but it did not exist.
   * <p>
   * e.g. {@code /playlists/1/songs/4} implying the {@code Playlist} with the id {@code 1} exists
   */
  @ExceptionHandler(AssertedResourceNotFoundException.class)
  public ResponseEntity<?> assertedResourceNotFound(AssertedResourceNotFoundException exception) {
    String requestPath = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
    // TODO [design] Read more on RFC7807 Problem Details.
    String problemDetail = String.format(
            "The implied resource (%s ID:%s) in the request requestPath '%s' does not exist. " +
                    "The validity of the target resource cannot be determined.",
            exception.getResourceName(),
            exception.getIdentifier(),
            requestPath);
    return ResponseEntity.badRequest().body(problemDetail);
  }

}
