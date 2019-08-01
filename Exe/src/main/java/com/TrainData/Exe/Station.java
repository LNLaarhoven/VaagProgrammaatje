package com.TrainData.Exe;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "Station")
public class Station {

	@Id
	private String naam;
	
	private String code;
	
	@OneToMany
	private Set<Trein> treinen;
	
	Station(String naam){
		this.naam = naam;
		this.treinen = new HashSet<>();
	}
	
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

	public Set<Trein> getTreinen() {
		return treinen;
	}

	public void setTreinen(Set<Trein> treinen) {
		this.treinen = treinen;
	}

}
