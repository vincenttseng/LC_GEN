package com.vincent.coretest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "single" })
public class ApiTest {
	protected final Logger log = LoggerFactory.getLogger(ApiTest.class);

	@SuppressWarnings("unused")
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void createClientSuccessfuly() throws Exception {
		log.info("createClientSuccessfuly");
	}
}
