package com.TrainData.Exe;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "Station")
public class Station {

	Station(String naam){
		this.naam = naam;
	}

	@Id
	private String naam;
	
	private String code;
	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	private Trein[] treinen;
	
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

	public Trein[] getTreinen() {
		return treinen;
	}

	public void setTreinen(Trein[] treinen) {
		this.treinen = treinen;
	}

}
