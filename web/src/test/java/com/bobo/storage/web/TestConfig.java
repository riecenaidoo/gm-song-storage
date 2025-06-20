package com.bobo.storage.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 *
 *
 * <ol>
 *   <li>It seems the easiest way to set up SpringBoot tests is to point it them to a {@link
 *       SpringBootApplication}. The {@code Web} module is just a library, so I need a target for
 *       the {@link WebMvcTest}(s) to configure from.
 * </ol>
 */
@SpringBootApplication
public class TestConfig {

	public static MockHttpServletRequest testSchemeAuthority(
			MockHttpServletRequest mockHttpServletRequest) {
		mockHttpServletRequest.setScheme("http");
		mockHttpServletRequest.setServerName("localhost");
		mockHttpServletRequest.setServerPort(8080);
		return mockHttpServletRequest;
	}

	public static String testSchemeAuthority() {
		return String.format("%s://%s:%d", "http", "localhost", 8080);
	}
}
