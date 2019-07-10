package com.bdwise.pSonar.model.event;

import com.bdwise.pSonar.model.PerfSonarEvent;
import com.bdwise.pSonar.model.result.ThroughputResult;

public class ThroughputEvent extends PerfSonarEvent<ThroughputResult> {

	public ThroughputEvent(Object source, boolean succeeded, ThroughputResult message) {
		super(source, succeeded, message);
	}

}
