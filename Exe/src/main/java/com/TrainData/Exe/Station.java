package com.TrainData.Exe;

import java.util.ArrayList;

public class Station {
	String naam;
	String code;
	private ArrayList<Trein> treinen = new ArrayList<>();

	public ArrayList<Trein> getTreinen() {
		return treinen;
	}

	public void setTreinen(ArrayList<Trein> treinen) {
		this.treinen = treinen;
	}
}
