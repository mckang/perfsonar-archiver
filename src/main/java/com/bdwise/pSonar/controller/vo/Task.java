package com.bdwise.pSonar.controller.vo;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

@JsonAdapter(Task.TaskTypeAdapter.class)
public class Task {
	private String id;
	private String repeat;
	private String tool;
	private String type;
	private String destination;
	private int packetCount;
	private boolean enabled;	
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getRepeat() {
		return repeat;
	}
	
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	
	public String getTool() {
		return tool;
	}
	
	public void setTool(String tool) {
		this.tool = tool;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getDestination() {
		return destination;
	}
	
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	public int getPacketCount() {
		return packetCount;
	}
	
	public void setPacketCount(int packetCount) {
		this.packetCount = packetCount;
	}
	
	public static class TaskTypeAdapter extends TypeAdapter<Task> {

		@Override
		public void write(JsonWriter out, Task value) throws IOException {

		}

		@Override
		public Task read(JsonReader in) throws IOException {
			Task task = new Task();
			in.beginObject();
			while (in.hasNext()) {
				String name = in.nextName();
				switch (name) {
				case "schedule":
					in.beginObject();
					while (in.hasNext()) {
						name = in.nextName();
						switch (name) {
						case "repeat":
							task.setRepeat(in.nextString());
							break;
						default:
							in.skipValue();	
						}
					}
					in.endObject();
					break;
				case "tool":
					task.setTool(in.nextString());
					break;	
				case "detail":
					in.beginObject();
					while (in.hasNext()) {
						name = in.nextName();
						switch (name) {
						case "enabled":
							task.setEnabled(in.nextBoolean());;
							break;
						default:
							in.skipValue();	
						}
					}
					in.endObject();
					break;					
				case "href":
					String href = in.nextString();
					int substringIndex = href.lastIndexOf('/');
					task.setId(href.substring(substringIndex+1));
					break;	
				case "test":
					in.beginObject();
					while (in.hasNext()) {
						name = in.nextName();
						switch (name) {
						case "spec":
							in.beginObject();
							while (in.hasNext()) {
								name = in.nextName();
								switch (name) {
								case "dest":
									task.setDestination(in.nextString());
									break;
								case "packet-count":
									task.setPacketCount(in.nextInt());
									break;	
								case "count":
									task.setPacketCount(in.nextInt());
									break;										
								default:
									in.skipValue();	
								}
							}
							in.endObject();
							break;
						case "type":
							task.setType(in.nextString());
							break;
						default:
							in.skipValue();	
						}
					}
					in.endObject();					
					break;						
				default:
					in.skipValue();	
				}
			}
			in.endObject();
			return task;
		}
		
	}
	
}
