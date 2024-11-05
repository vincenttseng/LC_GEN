package com.vincent.coretest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 
 * com.vincent.coretest DeviceSchedulerApp.java
 *
 * @author vincenttseng 2019年7月22日
 */
@SpringBootApplication
@ComponentScan("com.vincent.coretest")
public class DeviceSchedulerApp {
	protected static final Logger log = LoggerFactory.getLogger(DeviceSchedulerApp.class);

	private static int serverPort = 0;

	@Value("${server.port:9100}")
	public void setServerPort(int port) {
		serverPort = port;
	}

	public static void main(String[] args) {
		log.info("main");
		SpringApplication.run(DeviceSchedulerApp.class, args);
		log.info("SpringApplication is started on port: {}", serverPort);
	}
}
