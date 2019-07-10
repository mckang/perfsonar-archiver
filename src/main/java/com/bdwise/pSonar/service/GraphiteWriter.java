package com.bdwise.pSonar.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.bdwise.pSonar.model.event.LatencyEvent;
import com.bdwise.pSonar.model.event.RttEvent;
import com.bdwise.pSonar.model.event.ThroughputEvent;
import com.bdwise.pSonar.model.result.LatencyResult;
import com.bdwise.pSonar.model.result.RttResult;
import com.bdwise.pSonar.model.result.ThroughputResult;

@Component
public class GraphiteWriter implements InitializingBean{

	@Value("${carbon.server.address}")
	private String carbonServerAddress;
	
	@Value("${carbon.server.port}")
	private int carbonServerPort;
	
	private SimpleGraphiteClient graphiteClient;
	
	@Async
    @EventListener
    public void handleLatencyEvent(LatencyEvent latencyEvent) {
		LatencyResult latencyResult = latencyEvent.getMessage();
		String destination = latencyResult.getDestination().replaceAll("\\.", "_");
		Map<String, Number> allAnswers = new HashMap<String, Number>() {{
			put(String.format("pSonar.latency.%s.packet-received.%s",latencyResult.getTool(), destination), latencyResult.getPacketsReceived());
			put(String.format("pSonar.latency.%s.packet-sent.%s",latencyResult.getTool(), destination), latencyResult.getPacketsSent());
			put(String.format("pSonar.latency.%s.packet-reordered.%s",latencyResult.getTool(), destination), latencyResult.getPacketsReorded());
			put(String.format("pSonar.latency.%s.packet-lost.%s",latencyResult.getTool(), destination), latencyResult.getPacketsLost());

		}};
		
		if(latencyEvent.isSucceeded()) {
			DescriptiveStatistics stats = new DescriptiveStatistics();
			latencyResult.getLatencyList().stream().forEach(stats::addValue);
	
			allAnswers.put(String.format("pSonar.latency.%s.delay.min.%s",latencyResult.getTool(), destination), stats.getMin());
			allAnswers.put(String.format("pSonar.latency.%s.delay.max.%s",latencyResult.getTool(), destination), stats.getMax());
			allAnswers.put(String.format("pSonar.latency.%s.delay.mean.%s",latencyResult.getTool(), destination), stats.getMean());
			allAnswers.put(String.format("pSonar.latency.%s.delay.95.%s",latencyResult.getTool(), destination), stats.getPercentile(0.95));
		}

		
		graphiteClient.sendMetrics(allAnswers);
	}
	
	@Async	
    @EventListener
    public void handleThroughputEvent(ThroughputEvent throughputEvent) {
		ThroughputResult throughputResult = throughputEvent.getMessage();
		String destination = throughputResult.getDestination().replaceAll("\\.", "_");
		
		Map<String, Number> allAnswers = new HashMap<String, Number>() {{
			put(String.format("pSonar.throughput.%s.bits.%s",throughputResult.getTool(), destination), throughputResult.getThroughputBits());
			put(String.format("pSonar.throughput.%s.bytes.%s",throughputResult.getTool(), destination), throughputResult.getThroughputBytes());
			put(String.format("pSonar.throughput.%s.retransmits.%s",throughputResult.getTool(), destination), throughputResult.getRetransmits());

		}};
		
		graphiteClient.sendMetrics(allAnswers);

    }
	
	@Async	
    @EventListener
    public void handleRttEvent(RttEvent rttEvent) {
		RttResult rttResult = rttEvent.getMessage();
		String destination = rttResult.getDestination().replaceAll("\\.", "_");
		Map<String, Number> allAnswers = new HashMap<String, Number>() {{
			put(String.format("pSonar.rtt.%s.received.%s",rttResult.getTool(), destination), rttResult.getReceived());
			put(String.format("pSonar.rtt.%s.loss.%s",rttResult.getTool(), destination), rttResult.getLoss());
			put(String.format("pSonar.rtt.%s.lost.%s",rttResult.getTool(), destination), rttResult.getLost());
			put(String.format("pSonar.rtt.%s.reorders.%s",rttResult.getTool(), destination), rttResult.getReorders());

		}};
		
		if(rttEvent.isSucceeded()) {
			DescriptiveStatistics stats = new DescriptiveStatistics();
			rttResult.getRttList().stream().forEach(stats::addValue);
	
			allAnswers.put(String.format("pSonar.rtt.%s.delay.min.%s",rttResult.getTool(), destination), stats.getMin());
			allAnswers.put(String.format("pSonar.rtt.%s.delay.max.%s",rttResult.getTool(), destination), stats.getMax());
			allAnswers.put(String.format("pSonar.rtt.%s.delay.mean.%s",rttResult.getTool(), destination), stats.getMean());
			allAnswers.put(String.format("pSonar.rtt.%s.delay.95.%s",rttResult.getTool(), destination), stats.getPercentile(0.95));
		}

		
		graphiteClient.sendMetrics(allAnswers);
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		graphiteClient = new SimpleGraphiteClient(this.carbonServerAddress,this.carbonServerPort);
	}
}
