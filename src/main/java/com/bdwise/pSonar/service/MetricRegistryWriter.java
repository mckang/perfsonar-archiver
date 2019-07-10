package com.bdwise.pSonar.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.bdwise.pSonar.model.AbstractResult;
import com.bdwise.pSonar.model.event.LatencyEvent;
import com.bdwise.pSonar.model.event.RttEvent;
import com.bdwise.pSonar.model.event.ThroughputEvent;
import com.bdwise.pSonar.model.result.LatencyResult;
import com.bdwise.pSonar.model.result.RttResult;
import com.bdwise.pSonar.model.result.ThroughputResult;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;

@Component
public class MetricRegistryWriter {
	private final static Logger logger = LoggerFactory.getLogger(MetricRegistryWriter.class);
	
	@Autowired
	MeterRegistry registry;
    
	private List<Tag> getLabels(AbstractResult result, boolean succeeded) {
		List<Tag> tags = new ArrayList<>();
		tags.add(Tag.of("destination", result.getDestination()));
		tags.add(Tag.of("tool", result.getTool()));
		tags.add(Tag.of("succeeded", succeeded+""));
		return tags;
	}	
    
	@Async
    @EventListener
    public void handleLatencyEvent(LatencyEvent latencyEvent) {
		LatencyResult latencyResult = latencyEvent.getMessage();
		logger.debug(Thread.currentThread().getName() + "\n"+ latencyResult.toString());



		List<Tag> tags = getLabels(latencyResult, latencyEvent.isSucceeded());
		registry.gauge("latency.packet-received", tags, latencyResult.getPacketsReceived());
		registry.gauge("latency.packet-sent", tags, latencyResult.getPacketsSent());
		registry.gauge("latency.packet-reordered", tags, latencyResult.getPacketsReorded());
		registry.gauge("latency.packet-lost", tags, latencyResult.getPacketsLost());

		if (latencyEvent.isSucceeded()) {
			DistributionSummary distributionSummary = DistributionSummary.builder("latency.delay").baseUnit("ms")
					.tags(tags).register(registry);

			latencyResult.getLatencyList().stream().forEach(distributionSummary::record);
			distributionSummary.close();
		}
    }
    
	@Async	
    @EventListener
    public void handleRttEvent(RttEvent rttEvent) {
		RttResult rttResult = rttEvent.getMessage();
		logger.debug(Thread.currentThread().getName() + "\n"+ rttResult.toString());


		List<Tag> tags = getLabels(rttResult, rttEvent.isSucceeded());
		registry.gauge("rtt.packet-received", tags, rttResult.getReceived());
		registry.gauge("rtt.packet-loss", tags, rttResult.getLoss());
		registry.gauge("rtt.packet-reordered", tags, rttResult.getReorders());
		registry.gauge("rtt.packet-lost", tags, rttResult.getLost());

		if (rttEvent.isSucceeded()) {
			DistributionSummary distributionSummary = DistributionSummary.builder("rtt.delay")
					.publishPercentileHistogram().baseUnit("ms").tags(tags).register(registry);

			rttResult.getRttList().stream().forEach(distributionSummary::record);
			distributionSummary.close();
		}
    }
	
    
	@Async	
    @EventListener
    public void handleThroughputEvent(ThroughputEvent throughputEvent) {
		ThroughputResult throughputResult = throughputEvent.getMessage();
		logger.debug(Thread.currentThread().getName() + "\n"+ throughputResult.toString());


		List<Tag> tags = getLabels(throughputResult, throughputEvent.isSucceeded());
		registry.gauge("throughput.bits", tags, throughputResult.getThroughputBits());
		registry.gauge("throughput.bytes", tags, throughputResult.getThroughputBytes());
		registry.gauge("throughput.retransmits", tags, throughputResult.getRetransmits());

    }
    
}
