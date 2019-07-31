package com.TrainData.Exe;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Station")
public class Station {

	Station(String naam){
		this.naam = naam;
	}

	@Id
	private String naam;
	
	private String code;
	private ArrayList<Trein> treinen = new ArrayList<>();
	
	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ArrayList<Trein> getTreinen() {
		return treinen;
	}

	public void setTreinen(ArrayList<Trein> treinen) {
		this.treinen = treinen;
	}
}
