package com.TrainData.Exe;

import java.util.Timer;

import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ExeApplication {
	static Timer timer = new Timer();
	public static void main(String[] args) {
		timer.schedule(new Updater(), 0, 1000*15);
	}

}

