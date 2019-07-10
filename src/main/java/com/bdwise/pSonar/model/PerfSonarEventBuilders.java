package com.bdwise.pSonar.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import com.bdwise.pSonar.model.event.LatencyEvent;
import com.bdwise.pSonar.model.event.RttEvent;
import com.bdwise.pSonar.model.event.ThroughputEvent;
import com.bdwise.pSonar.model.result.LatencyResult;
import com.bdwise.pSonar.model.result.RttResult;
import com.bdwise.pSonar.model.result.ThroughputResult;
import com.google.gson.Gson;

public enum PerfSonarEventBuilders {

	LATENCY("latency", LatencyEvent.class){
		public LatencyEvent build(Object source, boolean succeeded, String msgBody) {
			Gson gson = new Gson();
			LatencyResult latencyResult = gson.fromJson(msgBody, LatencyResult.class);
			return new LatencyEvent(source, succeeded, latencyResult);
		}
	},
	RTT("rtt", RttEvent.class){
		public RttEvent build(Object source, boolean succeeded, String msgBody) {
			Gson gson = new Gson();
			RttResult rttResult = gson.fromJson(msgBody, RttResult.class);
			return new RttEvent(source, succeeded, rttResult);
		}
		
	},
	THROUGHPUT("throughput", ThroughputEvent.class){
		public ThroughputEvent build(Object source, boolean succeeded, String msgBody) {
			Gson gson = new Gson();
			ThroughputResult throughputResult = gson.fromJson(msgBody, ThroughputResult.class);
			return new ThroughputEvent(source, succeeded, throughputResult);
		}
		
	};
	
	private String text;
	private Class<? extends PerfSonarEvent> clazz;

	PerfSonarEventBuilders(String text, Class<? extends PerfSonarEvent> clazz) {
        this.text = text;
        this.clazz = clazz;
    }

	
	public <T extends PerfSonarEvent> T build(Object source, boolean succeeded, String msgBody) {
		T event = null;
		try {
			Constructor<? extends PerfSonarEvent> constructor = clazz.getDeclaredConstructor(Object.class, boolean.class, String.class);
			event = (T) constructor.newInstance(source, succeeded, msgBody);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return event;
	}
	
    public String getText() {
        return this.text;
    }

    public static Optional<PerfSonarEventBuilders> fromString(String text) {
        for (PerfSonarEventBuilders b : PerfSonarEventBuilders.values()) {
            if (b.text.equalsIgnoreCase(text)) {
                return Optional.of(b);
            }
        }
        return Optional.empty();
    }
}
