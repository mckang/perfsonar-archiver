package com.bdwise.pSonar.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bdwise.pSonar.model.PerfSonarEventBuilders;
import com.jayway.jsonpath.JsonPath;

@RestController
@RequestMapping("/data")
public class ArchiverController {

	private final static Logger logger = LoggerFactory.getLogger(ArchiverController.class);

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

	@PostMapping(value = "/", produces = "application/json")
	public String archive(@RequestBody String taskResult) {
		logger.debug(taskResult);

		boolean succeeded = JsonPath.read(taskResult, "$.result.succeeded");
		String type = JsonPath.read(taskResult, "$.test.type");
		logger.debug(succeeded + ":" + type);
		
		PerfSonarEventBuilders  eventsBuilder = PerfSonarEventBuilders.fromString(type).get();
		applicationEventPublisher.publishEvent(eventsBuilder.build(this, succeeded, taskResult));
		
		return "{\"result\" : \"OK\"}";
	}
}
