package com.TrainData.Exe;

import com.TrainData.JSON.*;

import java.time.LocalDateTime;
import java.util.TimerTask;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class Updater extends TimerTask {
	String myStation = "ASD"; //THE NAME OF THE STATION AS NS KNOWS IT
	Station station = new Station(myStation); //MAKES A OBJECT OF THE STATION
	int maxJourneys = 50; //LISTS THE MAX AMOUNT OF JOURNEYS THE API MAY PULL
	String databaseUrl = "http://localhost:8080/api/treinen"; //ADDRESS OF OWN DATABASE

	//RUNS THIS FUNCTION WITH A SCEDULED TIMERTASK, ACTIVATION EVERY *** MILLIS
	public void run() {
		updateAlleTreinen(LocalDateTime.now().toString(), myStation);
	}
	
	//HANDLES ALL THE FUNCTIONS NEEDED TO GET TRAIN DATA AND SEND IT TO OUR OWN DATABASE
	void updateAlleTreinen(String time, String station) {
		Trein[] trein;
		Arrivals[] arrivals = CRUDStationTreinen(time, station);
		trein = this.station.getTreinen();
		trein = handelUpdatesEnNieuweTreinen(arrivals,trein);
		this.station.setTreinen(trein);
	}
	
	//CONTROLS ALL THE TRAINS IT FOUND FROM THE NS API AND SHOWS IF THEY ARE NEW TO THE PROGRAM AND WEITHER THEIR TIMES HAVE BEEN UPDATED
	private Trein[] handelUpdatesEnNieuweTreinen(Arrivals[] arrivals, Trein[] trein){
		if(trein == null) {
			trein = new Trein[1];
			trein[0] = new Trein("demo Trein", "demo Origin", new String[] {LocalDateTime.now().toString()}, new String[] {LocalDateTime.now().toString()});
		}
		boolean updates = false;
		for (int i = 0; i < arrivals.length; i++) {
			if (!trein[0].getNaam().equals("demo Trein")) {
				boolean nieuweTrein = true;
				for (int j = 0; j < trein.length; j++) {
					if (arrivals[i].getName().equals(trein[j].getNaam())
							&& arrivals[i].getOrigin().equals(trein[j].getOrigin())) {
						/* check if the actualDateTime still matches, else update */
						nieuweTrein = false; /* Een bekende trein gevonden */
						if(LocalDateTime.parse(trein[j].getWerkelijkeAankomsten()[0]).compareTo(LocalDateTime.parse(arrivals[i].getActualDateTime())) > 0) {
						updates = true;
						systemOutUpdate(trein[j], arrivals[i]);
						trein[j].setWerkelijkeAankomsten(new String[] {arrivals[i].getActualDateTime()});
						}
					}
				}
				if (nieuweTrein) {
					/* nieuwe trein */
					updates = true;
					systemOutNieuwTrein(arrivals[i]);
					Trein[] treinen = new Trein[trein.length+1];
					for(int n = 0; n < treinen.length; n++) {
						if(n<trein.length) {
						treinen[n] = trein[n];
						} else {
							treinen[n] = addNewTrain(arrivals[i]);
						}
					}
					trein = treinen;
					//trein.add(addNewTrain(arrivals[i]));
				}
			} else {
				updates = true;
				systemOutNieuwTrein(arrivals[i]);
				
				Trein[] treinen = new Trein[trein.length+1];
				for(int n = 0; n < treinen.length; n++) {
					if(n<trein.length) {
					treinen[n] = trein[n];
					} else {
						treinen[n] = addNewTrain(arrivals[i]);
					}
				}
				trein = treinen;
				//trein.add(addNewTrain(arrivals[i]));
			}

		}
		if(!updates) {
			System.out.println("No new updates");
		}
		return trein;
	}
	
	//GETS ALL THE TRAINS IN THE COMING 2 HOURS FROM THE NS API (REQUIRES OBJECTS TO FIT THE INCOMING DATA)
	private Arrivals[] CRUDStationTreinen(String time, String station) {
		// HTTP GET REQUIREMENTS
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Ocp-Apim-Subscription-Key", "d5ae16043d844abc9ad1db166ae5145f");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<ThisStation> response = restTemplate
				.exchange(
						"https://gateway.apiportal.ns.nl/public-reisinformatie/api/v2/arrivals?" + time
								+ "&maxJourneys="+maxJourneys+"&lang=nl&station=" + station,
						HttpMethod.GET, entity, ThisStation.class);
		return response.getBody().getPayload().getArrivals();
		
	}
	
	//MAKES A NEW TRAIN OBJECT, CAN BE USED TO ADD TO A ARRAYLIST
	private Trein addNewTrain(Arrivals arrival) {
		String[] arrivalPlannedDateTime = new String[1];
		String[] arrivalActualDateTime = new String[1];
		Station[] localStation = new Station[1];
		arrivalPlannedDateTime[0] = arrival.getPlannedDateTime();
		arrivalActualDateTime[0] = arrival.getActualDateTime();
		localStation[0]= new Station(myStation);
		Trein trein = new Trein(arrival.getName(), arrival.getOrigin(), arrivalPlannedDateTime, arrivalActualDateTime);
		CRUDPostTreinNaarDatabase(trein);
		return trein;
	}
	
	//GIVES BACK A STATUS TO THE CONSOLE INFORMING IT THAT A TRAIN TIME HAS BEEN UPDATED
	private void systemOutUpdate(Trein trein, Arrivals arrivals) {
		System.out.print("Update: ");
		System.out.print("Origin: " + arrivals.getOrigin());
		System.out.print(" previous ActualDateTime: " + trein.getWerkelijkeAankomsten()[0]);
		System.out.println(" new ActualDateTime: "+arrivals.getActualDateTime());
		systemOutTreinTeLaat(arrivals);
	}
	
	//GIVES BACK A STATUS TO THE CONSOLE INFORMING IT THAT A NEW TRAIN HAS BEEN MADE
	private void systemOutNieuwTrein(Arrivals arrivals) {
		System.out.print("New: ");
		System.out.print("Origin: " + arrivals.getOrigin());
		System.out.print(" Name: "+ arrivals.getName());
		System.out.print(" PlannedDateTime: " + arrivals.getPlannedDateTime());
		System.out.println(" ActualDateTime: " + arrivals.getActualDateTime());
		systemOutTreinTeLaat(arrivals);
	}
	
	//GIVES BACK A STATUS TO THE CONSOLE INFORMING IT THAT A TRAIN IS LATER THAN SCEDULED
	private void systemOutTreinTeLaat(Arrivals arrivals) {
		if(LocalDateTime.parse(arrivals.getPlannedDateTime()).isBefore(LocalDateTime.parse(arrivals.getActualDateTime()))) {
			System.out.println("TE LAAT! Dat gaat niet goed!");			
		}
	}
	
	private void CRUDPostTreinNaarDatabase(Trein trein) {
		// HTTP POST REQUIREMENTS	
		HttpEntity<Trein> entity = new HttpEntity<Trein>(trein);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.exchange(databaseUrl,HttpMethod.POST, entity, Trein.class);
	}
}