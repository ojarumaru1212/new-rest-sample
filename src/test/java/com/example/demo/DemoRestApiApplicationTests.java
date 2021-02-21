package com.example.demo;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@WebAppConfiguration
class DemoRestApiApplicationTests {

	@Mock
	Environment env;

	@Test
	void contextLoads() {
		MockEnvironment environment = new MockEnvironment();
	    environment.setProperty("admin.api.url", "/admin");
	    environment.setProperty("admin.i18n.path", "/admin/i18n");
	    environment.setProperty("admin.debug", "true");
//	    applicationContext.setEnvironment(environment);
	}

}
