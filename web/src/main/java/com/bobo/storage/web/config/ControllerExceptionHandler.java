package com.bobo.storage.web.config;

import com.bobo.storage.web.exception.AssertedResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(AssertedResourceNotFoundException.class)
  public ResponseEntity<?> assertedResourceNotFound(AssertedResourceNotFoundException exception) {
    String requestPath = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
    String problemDetail = exception.problemDetail(requestPath);
    return ResponseEntity.badRequest().body(problemDetail);
  }

}
