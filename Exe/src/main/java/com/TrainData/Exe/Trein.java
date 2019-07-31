package com.TrainData.Exe;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Trein")
public class Trein {
	
	@Id
	private String naam;
	
	private String origin;
	private ArrayList<LocalDateTime> geplandeAankomsten;
	private ArrayList<LocalDateTime> werkelijkeAankomsten;
	private ArrayList<Station> stations;

	// CONSTRUCTORS
	public Trein(String naam, String origin, ArrayList<LocalDateTime> geplandeAankomsten,
			ArrayList<LocalDateTime> werkelijkeAankomsten, ArrayList<Station> station) {
		this.naam = naam;
		this.origin = origin;
		this.geplandeAankomsten = geplandeAankomsten;
		this.werkelijkeAankomsten = werkelijkeAankomsten;
		this.stations = station;
	}

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public ArrayList<LocalDateTime> getGeplandeAankomsten() {
		return geplandeAankomsten;
	}

	public void setGeplandeAankomsten(ArrayList<LocalDateTime> geplandeAankomsten) {
		this.geplandeAankomsten = geplandeAankomsten;
	}
	
	public void setSingleGeplandeAankomsten(LocalDateTime geplandeAankomsten, int i) {
		this.geplandeAankomsten.set(i,geplandeAankomsten);
	}

	public ArrayList<LocalDateTime> getWerkelijkeAankomsten() {
		return werkelijkeAankomsten;
	}

	public void setWerkelijkeAankomsten(ArrayList<LocalDateTime> werkelijkeAankomsten) {
		this.werkelijkeAankomsten = werkelijkeAankomsten;
	}
	
	public void setSingleWerkelijkeAankomsten(int i, LocalDateTime geplandeAankomsten) {
		this.geplandeAankomsten.set(i,geplandeAankomsten);
	}

	public ArrayList<Station> getStations() {
		return stations;
	}

	public void setStations(ArrayList<Station> stations) {
		this.stations = stations;
	}
	
	public boolean getTeLaat(String station) {
		for(int i = 0; i < stations.size(); i++) {
			if(stations.get(i).getNaam().equals(station)) {
				return geplandeAankomsten.get(i).isBefore(werkelijkeAankomsten.get(i));
			}
		}
		return false;
	}
}
