package com.TrainData.Exe;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TimerTask;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.annotation.*;

public class Updater extends TimerTask {
	String myStation = "ASD";
	Station station = new Station();
	int maxJourneys = 50;

	public void run() {
		updateAlleTreinen(LocalDateTime.now(), myStation);
	}

	void updateAlleTreinen(LocalDateTime time, String station) {
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

		ArrayList<Trein> trein;
		Arrivals[] arrivals = response.getBody().payload.arrivals;
		trein = this.station.getTreinen();
		boolean updates = false;
		for (int i = 0; i < arrivals.length; i++) {
			if (trein.size() > 0) {
				boolean nieuweTrein = true;
				for (int j = 0; j < trein.size(); j++) {
					if (arrivals[i].name.equals(trein.get(j).naam)
							&& arrivals[i].origin.equals(trein.get(j).origin)) {
						/* check if the actualDateTime still matches, else update */
						nieuweTrein = false; /* Een bekende trein gevonden */
						if(trein.get(j).werkelijkeAankomsten[0].compareTo(arrivals[i].actualDateTime) > 0) {
						updates = true;
						System.out.print("Update: ");
						System.out.print("Origin: " + arrivals[i].origin);
						System.out.print(" previous ActualDateTime: " + trein.get(j).werkelijkeAankomsten[0]);
						System.out.println(" new ActualDateTime: "+arrivals[i].actualDateTime);
						trein.get(j).werkelijkeAankomsten[0] = arrivals[i].actualDateTime;
						}
					}
				}
				if (nieuweTrein) {
					/* nieuwe trein */
					updates = true;
					trein.add(new Trein(arrivals[i].name, arrivals[i].origin, arrivals[i].cancelled,
							new LocalDateTime[] { arrivals[i].plannedDateTime },
							new LocalDateTime[] { arrivals[i].actualDateTime }));
					System.out.print("New: ");
					System.out.print("Origin: " + arrivals[i].origin);
					System.out.print(" Name: "+ arrivals[i].name);
					System.out.print(" PlannedDateTime: " + arrivals[i].plannedDateTime);
					System.out.println(" ActualDateTime: " + arrivals[i].actualDateTime);
					if(arrivals[i].plannedDateTime.isBefore(arrivals[i].actualDateTime)) {
						System.out.println("TE LAAT! Dat gaat niet goed!");
						
					}
				}
			} else {
				updates = true;
				trein.add(new Trein(arrivals[i].name, arrivals[i].origin, arrivals[i].cancelled,
						new LocalDateTime[] { arrivals[i].plannedDateTime },
						new LocalDateTime[] { arrivals[i].actualDateTime }));
			}

		}
		if(!updates) {
			System.out.println("No new updates");
		}

		this.station.setTreinen(trein);
	}
}

@JsonIgnoreProperties(ignoreUnknown = true)
class ThisStation {
	Payload payload;

	public Payload getPayload() {
		return payload;
	}

	public void setPayload(Payload payload) {
		this.payload = payload;
	}

}

@JsonIgnoreProperties(ignoreUnknown = true)
class Payload {
	Arrivals[] arrivals;

	public Arrivals[] getArrivals() {
		return arrivals;
	}

	public void setArrivals(Arrivals[] arrivals) {
		this.arrivals = arrivals;
	}
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Arrivals {
	String origin;
	String name;
	boolean cancelled;
	LocalDateTime plannedDateTime;
	LocalDateTime actualDateTime;

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public String getPlannedDateTime() {
		return plannedDateTime.toString() + "+0200";
	}

	public void setPlannedDateTime(String plannedDateTime) {
		this.plannedDateTime = LocalDateTime.parse(plannedDateTime.substring(0, plannedDateTime.indexOf('+')));
	}

	public String getActualDateTime() {
		return actualDateTime.toString() + "+0200";
	}

	public void setActualDateTime(String actualDateTime) {
		this.actualDateTime = LocalDateTime.parse(actualDateTime.substring(0, actualDateTime.indexOf('+')));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

}