package com.bdwise.pSonar.model.result;

import java.io.IOException;

import com.bdwise.pSonar.model.AbstractResult;
import com.bdwise.pSonar.model.AbstractResultTypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;

@JsonAdapter(ThroughputResult.ThroughputResultTypeAdapter.class)
public class ThroughputResult extends AbstractResult {
	private double throughputBits;
	private double throughputBytes;
	private int retransmits;

	public double getThroughputBits() {
		return throughputBits;
	}

	public void setThroughputBits(double throughputBits) {
		this.throughputBits = throughputBits;
	}

	public double getThroughputBytes() {
		return throughputBytes;
	}

	public void setThroughputBytes(double throughputBytes) {
		this.throughputBytes = throughputBytes;
	}

	public int getRetransmits() {
		return retransmits;
	}

	public void setRetransmits(int retransmits) {
		this.retransmits = retransmits;
	}

	@Override
	public String toString() {
		return "ThroughputResult [throughputBits=" + throughputBits + ", throughputBytes=" + throughputBytes
				+ ", retransmits=" + retransmits + ", startTime=" + startTime + ", tool=" + tool + ", destination="
				+ destination + ", participants=" + participants + "]";
	}

	public static class ThroughputResultTypeAdapter extends AbstractResultTypeAdapter<ThroughputResult> {

		protected ThroughputResult newInstance() {
			return new ThroughputResult();
		}

		protected void doProcessResultElement(ThroughputResult throughputResult, JsonReader in) throws IOException {
			in.beginObject();
			while (in.hasNext()) {
				String _name = in.nextName();
				switch (_name) {
				case "summary":
					in.beginObject();
					while (in.hasNext()) {
						_name = in.nextName();
						switch (_name) {
						case "summary":
							in.beginObject();
							while (in.hasNext()) {
								_name = in.nextName();
								switch (_name) {
								case "throughput-bits":
									throughputResult.setThroughputBits(in.nextDouble());
									break;
								case "throughput-bytes":
									throughputResult.setThroughputBytes(in.nextDouble());
									break;
								case "retransmits":
									throughputResult.setRetransmits(in.nextInt());
									break;
								default:
									in.skipValue();
								}
							}
							in.endObject();
							break;
						default:
							in.skipValue();
						}
					}
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
