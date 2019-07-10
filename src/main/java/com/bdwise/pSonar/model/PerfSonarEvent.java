package com.bdwise.pSonar.model;

import org.springframework.context.ApplicationEvent;

public class PerfSonarEvent<T extends AbstractResult> extends ApplicationEvent{

	private boolean succeeded;
	private T message;
	public PerfSonarEvent(Object source, boolean succeeded, T message) {
		super(source);
		this.succeeded = succeeded;
		this.message = message;
	}
	public boolean isSucceeded() {
		return succeeded;
	}
	public T getMessage() {
		return message;
	}

}
