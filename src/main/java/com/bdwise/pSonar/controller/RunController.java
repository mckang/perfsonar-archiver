package com.bdwise.pSonar.controller;

import java.io.IOException;
import java.net.URI;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@RestController
@RequestMapping("/run")
public class RunController {

	@Value("${pscheduler.api.url}")
	private String pschedulerApiUrl = null;

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping(value = "/{taskId}", produces = "application/json")
	public List<String> search(@PathVariable(name = "taskId", required = true) String taskId) throws IOException {
		HttpHeaders headers = new HttpHeaders() {
			{
				set("Content-Type", "application/json");
			}
		};

		String isoDate = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);

		HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);

		URI uri = UriComponentsBuilder
				.fromUriString(pschedulerApiUrl + "/tasks/" + taskId + "/runs?limit=10&end=" + isoDate).build().encode()
				.toUri();

		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);

		Gson gson = new Gson();
		List<String> runs = gson.fromJson(response.getBody(), new TypeToken<List<String>>() {
		}.getType());

		return runs.stream().map(s -> {
			int substringIndex = s.lastIndexOf('/');
			return s.substring(substringIndex + 1);
		}).collect(Collectors.toList());
	}

	@GetMapping(value = "/{taskId}/{runId}", produces = "application/json")
	public String query(@PathVariable(name = "taskId", required = true) String taskId,
			@PathVariable(name = "runId", required = true) String runId) throws IOException {
		HttpHeaders headers = new HttpHeaders() {
			{
				set("Content-Type", "application/json");
			}
		};

		HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);

		URI uri = UriComponentsBuilder.fromUriString(pschedulerApiUrl + "/tasks/" + taskId + "/runs/" + runId).build()
				.encode().toUri();

		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);

		return response.getBody();
	}

}
