package com.bdwise.pSonar.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/graph")
public class GraphController {

	@Value("${graphite.server.url}")
	private String graphiteServerUrl = null;

	@GetMapping(value = "/", produces = "application/txt")
	public String homeChartUrl(@RequestParam(name = "width", defaultValue = "600", required = true) int width,
			@RequestParam(name = "height", defaultValue = "300", required = true) int height) {
		return String.format(graphiteServerUrl
				+ "/render?target=pSonar.latency.twping.delay.95.*&&from=-1h&lineMode=connected&format=png&width=%s&height=%s",
				width, height);
	}

	@GetMapping(value = "/delay/{destination}", produces = "application/txt")
	public String taskDelayChartUrl(@PathVariable(name = "destination", required = true) String destination,
			@RequestParam(name = "width", defaultValue = "600", required = true) int width,
			@RequestParam(name = "height", defaultValue = "300", required = true) int height) {
		return String.format(graphiteServerUrl
				+ "/render?target=pSonar.latency.twping.delay.*.%s&&from=-1h&lineMode=connected&width=%s&height=%s&format=png",
				destination.replaceAll("\\.", "_"), width, height);
	}

	@GetMapping(value = "/loss/{destination}", produces = "application/txt")
	public String taskLossChartUrl(@PathVariable(name = "destination", required = true) String destination,
			@RequestParam(name = "width", defaultValue = "600", required = true) int width,
			@RequestParam(name = "height", defaultValue = "300", required = true) int height) {
		return String.format(graphiteServerUrl
				+ "/render?target=pSonar.latency.twping.packet-*.%s&&from=-1h&lineMode=connected&width=%s&height=%s&format=png",
				destination.replaceAll("\\.", "_"), width, height);

	}
}
