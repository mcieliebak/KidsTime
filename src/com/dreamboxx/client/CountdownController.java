package com.dreamboxx.client;

import java.util.ArrayList;
import java.util.List;

public class CountdownController {

	private int startTime; //in minutes
	
	private List<CountdownListener> countdownListeners = new ArrayList<CountdownListener>();

	public int getStartTime() {
		System.out.println("CountdownController.getSTartTime: starttime is " + startTime);
		return startTime;
	}
	
	public void updateStartTime(int startTime) {
		System.out.println("CountdownController.updateSartTime: new starttime is " + startTime);

		this.startTime = startTime;
		for (CountdownListener cl : countdownListeners){
			cl.updateStartTime(startTime);
		}
	}
	
	public void startCountdown() {
		for (CountdownListener cl : countdownListeners){
			cl.startCountdown();
		}

	}

	public void finishCountdown() {
		for (CountdownListener cl : countdownListeners){
			cl.finishCountdown();
		}
	}


	public void addCountdownListener (CountdownListener cl) {
		countdownListeners.add(cl);
	}

	public void removeCountdownListener (CountdownListener cl) {
		countdownListeners.remove(cl);
	}
}

