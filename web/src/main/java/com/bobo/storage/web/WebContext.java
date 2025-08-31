package com.bobo.storage.web;

import static java.lang.annotation.ElementType.TYPE;

import com.bobo.semantic.ModuleContext;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * @implSpec {@link ModuleContext}
 */
@ModuleContext
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(WebConfig.class)
public @interface WebContext {}
