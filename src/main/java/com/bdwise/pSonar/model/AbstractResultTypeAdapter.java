package com.bdwise.pSonar.model;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public abstract class AbstractResultTypeAdapter<T extends AbstractResult>  extends TypeAdapter<T> {

	@Override
	public void write(JsonWriter out, T value) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public T read(JsonReader in) throws IOException {
		T result = newInstance();
		in.beginObject();
		while (in.hasNext()) {
			String name = in.nextName();
			switch(name) {
			case "id":
				in.skipValue();
				break;
			case "reference":
				in.skipValue();
				break;
			case "schedule":
				processScheduleElement(result, in);
				break;
			case "tool":
				processToolElement(result, in);
				break;
			case "participants":
				processParticipantsElement(result, in);
				break;
			case "result":	
				doProcessResultElement(result, in);
				break;
			case "test":	
				processTestElement(result, in);
				break;
			}
		}
		in.endObject();
		return result;
	}

	protected abstract void doProcessResultElement(T result, JsonReader in)  throws IOException;

	protected abstract T newInstance() ;
	
	private void processScheduleElement(T result, JsonReader in)  throws IOException {
		in.beginObject();
		while (in.hasNext()) {
			String _name = in.nextName();
			switch (_name) {
			case "duration":
				in.skipValue();
				break;
			case "start":
				result.setStartTime(in.nextString());
				break;
			default:
				in.skipValue();
			}
		}
		in.endObject();
	}
	
	private void processToolElement(T result, JsonReader in)  throws IOException {
		in.beginObject();
		while (in.hasNext()) {
			String _name = in.nextName();
			switch (_name) {
			case "version":
				in.skipValue();
				break;
			case "name":
				result.setTool(in.nextString());
				break;
			default:
				in.skipValue();
			}
		}
		in.endObject();			
		
	}

	
	private void processParticipantsElement(T result, JsonReader in)  throws IOException {
		in.beginArray();
		ArrayList<String> participants = new ArrayList<>();
		while (in.hasNext()) {
			participants.add(in.nextString());
		}
		result.setParticipants(participants);;
		in.endArray();
	}

	private void processTestElement(T result, JsonReader in)  throws IOException {
		in.beginObject();
		while (in.hasNext()) {
			String _name = in.nextName();
			switch (_name) {
			case "type":
				in.skipValue();
				break;
			case "spec": 
			{
				in.beginObject();
				while (in.hasNext()) {
					_name = in.nextName();
					switch (_name) {
					case "dest":
						result.setDestination(in.nextString());
						break;
					default:
						in.skipValue();
					}
				}
				in.endObject();	
				
				break;
			}
			default:
				in.skipValue();
			}
		}
		in.endObject();	
	}
	
}
