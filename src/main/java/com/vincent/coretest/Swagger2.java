/**
 * 
 */
package com.vincent.coretest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * com.vincent.coretest Swagger2.java
 *
 * @author vincenttseng 2019年7月22日
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
	protected final Logger log = LoggerFactory.getLogger(Swagger2.class);

	@Bean
	public Docket api() {
		log.info("api called {}", Swagger2.class.getPackage().getName());
		// @formatter:off
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage(Swagger2.class.getPackage().getName()))
				.paths(PathSelectors.any())
				.build();
		// @formatter:on
	}
}
