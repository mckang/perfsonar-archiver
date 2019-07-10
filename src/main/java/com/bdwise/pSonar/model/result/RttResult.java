package com.bdwise.pSonar.model.result;

import java.io.IOException;
import java.util.ArrayList;

import com.bdwise.pSonar.model.AbstractResult;
import com.bdwise.pSonar.model.AbstractResultTypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;

@JsonAdapter(RttResult.RttResultTypeAdapter.class)
public class RttResult extends AbstractResult {

	private int loss;
	private int lost;
	private int received;
	private int reorders;
	private ArrayList<Double> rttList;

	public int getLoss() {
		return loss;
	}

	public void setLoss(int loss) {
		this.loss = loss;
	}

	public int getLost() {
		return lost;
	}

	public void setLost(int lost) {
		this.lost = lost;
	}

	public int getReceived() {
		return received;
	}

	public void setReceived(int received) {
		this.received = received;
	}

	public int getReorders() {
		return reorders;
	}

	public void setReorders(int reorders) {
		this.reorders = reorders;
	}

	public ArrayList<Double> getRttList() {
		return rttList;
	}

	public void setRttList(ArrayList<Double> rttList) {
		this.rttList = rttList;
	}
	
	

	@Override
	public String toString() {
		return "RttResult [loss=" + loss + ", lost=" + lost + ", received=" + received + ", reorders=" + reorders
				+ ", rttList=" + rttList + ", startTime=" + startTime + ", tool=" + tool + ", destination="
				+ destination + ", participants=" + participants + "]";
	}



	public static class RttResultTypeAdapter extends AbstractResultTypeAdapter<RttResult> {

		@Override
		protected void doProcessResultElement(RttResult result, JsonReader in) throws IOException {
			in.beginObject();
			while (in.hasNext()) {
				String _name = in.nextName();
				switch (_name) {
				case "loss":
					result.setLoss(in.nextInt());
					break;
				case "lost":
					result.setLost(in.nextInt());
					break;
				case "received":
					result.setReceived(in.nextInt());
					break;
				case "reorders":
					result.setReorders(in.nextInt());
					break;
				case "roundtrips":
					in.beginArray();
					ArrayList<Double> rttList = new ArrayList<Double>();
					while (in.hasNext()) {
						in.beginObject();
						while (in.hasNext()) {
							_name = in.nextName();
							switch (_name) {
							case "rtt":
								String _value = in.nextString();
								rttList.add(Double.parseDouble(_value.substring(2, _value.length() - 1)) * 1000.0);
								break;
							default:
								in.skipValue();
							}
						}
						in.endObject();
					}
					result.setRttList(rttList);
					in.endArray();
					break;
				default:
					in.skipValue();
				}
			}
			in.endObject();
		}

		@Override
		protected RttResult newInstance() {
			return new RttResult();
		}

	}
}
