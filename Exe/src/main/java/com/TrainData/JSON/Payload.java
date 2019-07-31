package com.TrainData.JSON;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Payload {

	private Arrivals[] arrivals;

	public Arrivals[] getArrivals() {
		return arrivals;
	}

	public void setArrivals(Arrivals[] arrivals) {
		this.arrivals = arrivals;
	}
}
