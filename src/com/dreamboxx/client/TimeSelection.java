package com.dreamboxx.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

public class TimeSelection implements CountdownListener {

	private final int[] startTimes = {1, 2, 5, 10}; //start times in minutes
	private RadioButton starttimeSelectionButtons[];
	
	public TimeSelection(final CountdownController countdownController) {
		
		countdownController.addCountdownListener(this);

		
		// initialize start time buttons
		starttimeSelectionButtons= new RadioButton[startTimes.length];
		for (int i = 0; i < startTimes.length; i++) {
			starttimeSelectionButtons[i] = new RadioButton("Zeit", startTimes[i] + "Minuten");
		}

		
		
		//set default time
		starttimeSelectionButtons[0].setValue(true);


		
		//put start time buttons on UI
		HorizontalPanel buttonPanel = new HorizontalPanel(); // was "VerticalPanel" in last running version
		for (int i = 0; i < startTimes.length; i++) {
			buttonPanel.add(starttimeSelectionButtons[i]);
		}
		
		
		
		//put start button on UI
		ClickHandler startButtonHandler = new ClickHandler() {
			public void onClick(ClickEvent event) {
				for (int i = 0; i < startTimes.length; i++) {
					if (starttimeSelectionButtons[i].getValue()) {
						countdownController.updateStartTime(startTimes[i]);
					}
				}
				countdownController.startCountdown();
			}
		};

		Button startButton = new Button("Countdown starten");
		startButton.addClickHandler(startButtonHandler);
		buttonPanel.add(startButton);
		
		RootPanel.get("controlsContainer").add(buttonPanel);


	}




	@Override
	public void updateStartTime(int startTime) {
		//do nothing
	}

	@Override
	public void startCountdown() {
		//TODO: disable time selection
	}

	@Override
	public void finishCountdown() {
		//TODO: enable time selection
	}

}
