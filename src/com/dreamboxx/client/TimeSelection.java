package com.dreamboxx.client;

import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

public class TimeSelection implements CountdownListener {

	private final int[] startTimes = {1, 2, 5, 10}; //start times in minutes
	private RadioButton starttimeSelectionButtons[];
	private Button startButton;
	private Image startButtonImage;
	private Image startButtonImageDisabled;

	HorizontalPanel buttonPanel;

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
		buttonPanel = new HorizontalPanel(); // was "VerticalPanel" in last running version
		buttonPanel.setWidth("100%");
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

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

		//		Button startButton = new Button("Countdown starten");
		//		startButton.addClickHandler(startButtonHandler);
		//		buttonPanel.add(startButton);

		startButtonImageDisabled = new Image("images/startButtonDisabled.png");
		startButtonImageDisabled.setHeight("50px");
		startButtonImageDisabled.setWidth("50px");

		startButtonImage = new Image("images/startButton.png");
		startButtonImage.setHeight("50px");
		startButtonImage.setWidth("50px");


		startButton = new Button();
		startButton.addClickHandler(startButtonHandler);
		startButton.getElement().appendChild(startButtonImage.getElement());		

		buttonPanel.add(startButton);
		RootPanel.get("controlsContainer").add(buttonPanel);



		final Label l = new Label("1");
		Button up = new Button ("up");
		Button down = new Button ("down");

		up.addClickHandler(new ClickHandler () {
			public void onClick(ClickEvent event) {
				int currentTime = Integer.parseInt(l.getText());
				if (currentTime < CountdownParameters.NUMBER_OF_GROUNDS) {
					l.setText("" + (currentTime + 1));
				} else {
					//TODO: ignore click, notify user	
				}
			}
		});
		
		down.addClickHandler(new ClickHandler () {
			public void onClick(ClickEvent event) {
				int currentTime = Integer.parseInt(l.getText());
				if (currentTime > 0) {
					l.setText("" + (currentTime - 1));
				} else {
					//TODO: ignore click, notify user	
				}
			}
		});
			
		buttonPanel.add(l);
		buttonPanel.add(up);
		buttonPanel.add(down);
		
	}



	private void enableStartButton() {
		startButton.getElement().removeChild(startButtonImageDisabled.getElement());
		startButton.getElement().appendChild(startButtonImage.getElement());		
		startButton.setEnabled(true);
	}

	private void disableStartButton() {
		System.out.println("disable start button: button elems: " + startButton.getElement().getChildCount()); 
		startButton.getElement().removeChild(startButtonImage.getElement());
		startButton.getElement().appendChild(startButtonImageDisabled.getElement());		
		startButton.setEnabled(false);		
	}


	@Override
	public void updateStartTime(int startTime) {
		//do nothing
	}

	@Override
	public void startCountdown() {
		disableStartButton();
	}

	@Override
	public void finishCountdown() {
		enableStartButton();
	}

}
