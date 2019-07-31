package com.TrainData.Exe;

import java.time.LocalDateTime;

public class Trein {
String naam;
String origin;
boolean isTeLaat;
LocalDateTime[] geplandeAankomsten;
LocalDateTime[] werkelijkeAankomsten;
public Trein(String naam, String origin, boolean isTeLaat, LocalDateTime[] geplandeAankomsten,
		LocalDateTime[] werkelijkeAankomsten) {
	super();
	this.naam = naam;
	this.origin = origin;
	this.isTeLaat = isTeLaat;
	this.geplandeAankomsten = geplandeAankomsten;
	this.werkelijkeAankomsten = werkelijkeAankomsten;
}
}
