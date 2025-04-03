package com.bobo.storage.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

/**
 * <ol>
 *   <li>
 *     It seems the easiest way to set up SpringBoot tests is to point it them to a {@link SpringBootApplication}.
 *     The {@code Web} module is just a library, so I need a target for the {@link WebMvcTest}(s) to configure from.
 *   </li>
 * </ol>
 */
@SpringBootApplication
public class TestConfig {

}

