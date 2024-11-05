/**
 * 
 */
package com.vincent.coretest.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vincent.coretest.vo.ro.BasicResponeVO;

/**
 * 
 * com.vincent.coretest.controller HelloController.java
 *
 * @author vincenttseng 2019年7月22日
 */
@RestController
public class HelloController {
	protected final Logger log = LoggerFactory.getLogger(HelloController.class);

	protected List<Long> normalAttrIdList = new ArrayList<Long>();

	@GetMapping("/hello/")
	public Object hello() {
		log.info("hello");
		return "AAA";
	}
}
