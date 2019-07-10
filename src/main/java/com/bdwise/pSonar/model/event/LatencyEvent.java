package com.bdwise.pSonar.model.event;

import com.bdwise.pSonar.model.PerfSonarEvent;
import com.bdwise.pSonar.model.result.LatencyResult;

public class LatencyEvent extends PerfSonarEvent<LatencyResult> {

	public LatencyEvent(Object source, boolean succeeded, LatencyResult message) {
		super(source, succeeded, message);
	}

}
