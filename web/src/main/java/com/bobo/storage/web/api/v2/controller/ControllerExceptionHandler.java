package com.bobo.storage.web.api.v2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;

/**
 * "Our goal is to not handle exceptions explicitly in Controller methods where possible.
 * They are a cross-cutting concern better handled separately in dedicated code."
 *
 * @see <a href="https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc">Exception Handling in Spring MVC</a>
 */
@ControllerAdvice(name = "v2Handler")
public class ControllerExceptionHandler {

  /**
   * {@code 404 Not Found} if a {@code Resource} in the request path did not exist.
   * <p>
   * <ul>
   *   <li>
   *     e.g. {@code PATCH /playlists/1} requesting a {@code PATCH} operation on the {@code Playlist} with the id {@code 1},
   *    but it does not exist.
   *   </li>
   *   <li>
   *     e.g. {@code /playlists/1/songs/4} implying the {@code Playlist} with the id {@code 1} exists, but it did not.
   *   </li>
   * </ul>
   *
   * @see <a href="https://www.rfc-editor.org/rfc/rfc9110.html#section-15.5.5">404 Not Found</a>
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ProblemDetail resourceNotFound(ResourceNotFoundException resourceNotFound) {
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    String detail = String.format("The %s(ID:%s) resource in the request path does not exist.",
                                  resourceNotFound.getResourceName(),
                                  resourceNotFound.getIdentifier());
    problemDetail.setDetail(detail);
    problemDetail.setType(URI.create("https://www.rfc-editor.org/rfc/rfc9110.html#section-15.5.5"));

    return problemDetail;
  }

}
