package com.bdwise.pSonar.model.event;

import com.bdwise.pSonar.model.PerfSonarEvent;
import com.bdwise.pSonar.model.result.RttResult;

public class RttEvent extends PerfSonarEvent<RttResult>{

	public RttEvent(Object source, boolean succeeded, RttResult message) {
		super(source, succeeded, message);
	}

}
