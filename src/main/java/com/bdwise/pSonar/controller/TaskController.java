package com.bdwise.pSonar.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.bdwise.pSonar.controller.vo.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@RestController
@RequestMapping("/task")
public class TaskController {
	private final static Logger logger = LoggerFactory.getLogger(TaskController.class);

	@Value("${pscheduler.api.url}")
	private String pschedulerApiUrl = null;

	@Autowired
	private RestTemplate restTemplate;

	private MustacheFactory mf = new DefaultMustacheFactory("mustache");

	@GetMapping(value = "/", produces = "application/json")
	public List<Task> search() {

		HttpHeaders headers = new HttpHeaders() {
			{
				set("Content-Type", "application/json");
			}
		};
		HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);

		URI uri = UriComponentsBuilder
				.fromUriString(pschedulerApiUrl + "/tasks?expanded&detail&json={\"detail\": {\"enabled\": true}}")
				.build().encode().toUri();

		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);

		Gson gson = new Gson();
		List<Task> tasks = gson.fromJson(response.getBody(), new TypeToken<List<Task>>() {
		}.getType());

		return tasks;
	}

	@PostMapping(value = "/", produces = "application/json", consumes = "application/json")
	public Task regist(@RequestBody String body) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		Task task = mapper.readValue(body, Task.class);

		task.setType("latency");
		task.setTool("twping");

		Map<String, Object> params = new HashMap<>();
		params.put("repeat", task.getRepeat());
		params.put("destination", task.getDestination());
		params.put("packetCount", task.getPacketCount());
		params.put("type", task.getType());
		params.put("tool", task.getTool());
		Mustache m = mf.compile("createTask.ms");
		StringWriter writer = new StringWriter();
		m.execute(writer, params).flush();
		logger.debug(writer.toString());

		HttpHeaders headers = new HttpHeaders() {
			{
				set("Content-Type", "application/json");
			}
		};

		HttpEntity<String> requestEntity = new HttpEntity<String>(writer.toString(), headers);
		ResponseEntity<String> response = restTemplate.exchange(pschedulerApiUrl + "/tasks", HttpMethod.POST,
				requestEntity, String.class);
		String taskUrl = response.getBody().replaceAll("\"", "");
		System.out.println(taskUrl);
		int substringIndex = taskUrl.lastIndexOf('/');

		task.setId(taskUrl.substring(substringIndex + 1));
		return task;
	}

	@DeleteMapping(value = "/{taskId}", produces = "application/json")
	public String delete(@PathVariable(name = "taskId", required = true) String taskId) {

		HttpHeaders headers = new HttpHeaders() {
			{
				set("Content-Type", "application/json");
			}
		};

		HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
		ResponseEntity<String> response = restTemplate.exchange(pschedulerApiUrl + "/tasks/" + taskId,
				HttpMethod.DELETE, requestEntity, String.class);
		response.getStatusCode();

		return "{\"result\" : \"OK\"}";
	}
}
