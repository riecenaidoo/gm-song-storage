package com.bobo.storage.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @implNote Library modules do not have an actual {@link SpringBootApplication} for Spring to
 *     target when setting up {@link SpringBootTest}/{@code SliceTest(s)}, so we create a "fake"
 *     target {@link SpringBootApplication} for Spring to use when bootstrapping the application
 *     context.
 */
@SpringBootApplication
public class WebTestApplication {

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
