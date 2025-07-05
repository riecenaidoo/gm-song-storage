package com.bobo.storage;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfig.class)
class AppTest {

	@Test
	void contextLoads() {
		System.out.println("Application Context successfully loads during testing.");
	}
}
