package com.bobo.storage.web;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(WebConfig.class)
public @interface WebContext {}
