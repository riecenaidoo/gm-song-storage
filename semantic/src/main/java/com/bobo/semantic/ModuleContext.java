package com.bobo.semantic;

import java.lang.annotation.Documented;

/**
 * The primary configuration entry point for a module.
 *
 * <p>Serves as the single entry point for setting up the module within an application or test
 * context.
 *
 * @implSpec Module configurations should be encapsulated within the module ({@code
 *     package-private}), and be discovered and imported through the module's {@link ModuleContext}
 *     annotation.
 */
@Documented
public @interface ModuleContext {}
