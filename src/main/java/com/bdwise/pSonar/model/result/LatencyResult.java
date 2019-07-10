package com.bdwise.pSonar.model.result;

import java.io.IOException;
import java.util.ArrayList;

import com.bdwise.pSonar.model.AbstractResult;
import com.bdwise.pSonar.model.AbstractResultTypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;

@JsonAdapter(LatencyResult.LatencyResultTypeAdapter.class)
public class LatencyResult extends AbstractResult{
	private int packetsSent;
	private int packetsReorded;
	private int packetsLost;	
	private int packetsReceived;
	
	private ArrayList<Double> latencyList;

	
	public int getPacketsSent() {
		return packetsSent;
	}
	public void setPacketsSent(int packetsSent) {
		this.packetsSent = packetsSent;
	}
	public int getPacketsReorded() {
		return packetsReorded;
	}
	public void setPacketsReorded(int packetsReorded) {
		this.packetsReorded = packetsReorded;
	}
	public int getPacketsLost() {
		return packetsLost;
	}
	public void setPacketsLost(int packetsLost) {
		this.packetsLost = packetsLost;
	}
	public int getPacketsReceived() {
		return packetsReceived;
	}
	public void setPacketsReceived(int packetsReceived) {
		this.packetsReceived = packetsReceived;
	}
	public ArrayList<Double> getLatencyList() {
		return latencyList;
	}
	public void setLatencyList(ArrayList<Double> latencyList) {
		this.latencyList = latencyList;
	}
	@Override
	public String toString() {
		return "LatencyResult [startTime=" + startTime + ", tool=" + tool + ", destination=" + destination
				+ ", participants=" + participants + ", packetsSent=" + packetsSent + ", packetsReorded="
				+ packetsReorded + ", packetsLost=" + packetsLost + ", packetsReceived=" + packetsReceived
				+ ", latencyList=" + latencyList + "]";
	}
	
	public static class LatencyResultTypeAdapter extends AbstractResultTypeAdapter<LatencyResult> {

		protected LatencyResult newInstance() {
			return new LatencyResult();
		}
		
		protected void doProcessResultElement(LatencyResult latencyResult, JsonReader in)  throws IOException {
			in.beginObject();
			while (in.hasNext()) {
				String _name = in.nextName();
				switch(_name) {
				case "packets-sent":
					latencyResult.setPacketsSent(in.nextInt());
					break;
				case "packets-reordered":
					latencyResult.setPacketsReorded(in.nextInt());
					break;
				case "packets-received":
					latencyResult.setPacketsReceived(in.nextInt());
					break;
				case "packets-lost":
					latencyResult.setPacketsLost(in.nextInt());
					break;
				case "histogram-latency":
					in.beginObject();
					ArrayList<Double> latencyList = new ArrayList<Double>();
					while (in.hasNext()) {
						_name = in.nextName();
						int _value = in.nextInt();
						for(int i = 0; i< _value; i++) {
							latencyList.add(Double.parseDouble(_name));
						}
					}
					latencyResult.setLatencyList(latencyList);
					in.endObject();	
					break;
				default:
					in.skipValue();
				}
			}
			in.endObject();		
		}		
	}
}
