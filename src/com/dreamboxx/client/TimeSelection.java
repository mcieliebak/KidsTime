package com.dreamboxx.client;

import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

public class TimeSelection implements CountdownListener {

	private static final boolean IS_UP_BUTTON = true;
	private static final boolean IS_DOWN_BUTTON = false;

	private static final String BUTTON_IMAGE_PATH = "images/countdownButtons/";
	private final int[] startTimes = {1, 2, 5, 10}; //start times in minutes
	private RadioButton starttimeSelectionButtons[];

	private Button startButton;
	private Image startButtonImage;
	private Image startButtonImageDisabled;

	private Button upButton;
	private Button downButton;
	private Image upImage;
	private Image downImage;

	private Label starttimeSelectionLabel;

	HorizontalPanel buttonPanel;

	public TimeSelection(final CountdownController countdownController) {

		countdownController.addCountdownListener(this);


		starttimeSelectionLabel = new Label("1");
		starttimeSelectionLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		starttimeSelectionLabel.setStylePrimaryName("startTimeSelectionArea");



		// up and down buttons
		upImage = new Image(BUTTON_IMAGE_PATH + "up.png"); // TODO: this should be more generic
		upButton = createTimeSelectionButton(IS_UP_BUTTON, upImage);
		downImage = new Image(BUTTON_IMAGE_PATH + "down.png"); // TODO: this should be more generic
		downButton = createTimeSelectionButton(IS_DOWN_BUTTON, downImage);

		VerticalPanel upDownPanel = new VerticalPanel();
		upDownPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//		upDownPanel.setStylePrimaryName(countdownUpDownPanel);
		upDownPanel.setWidth("70px");
		upDownPanel.add(upButton);
		upDownPanel.add(downButton);


		HorizontalPanel timeSelectionPanel = new HorizontalPanel();
		timeSelectionPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		timeSelectionPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		timeSelectionPanel.add(starttimeSelectionLabel);
		timeSelectionPanel.add(upDownPanel);

		buttonPanel = new HorizontalPanel(); // was "VerticalPanel" in last running version
		buttonPanel.setWidth("30%");
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		buttonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		buttonPanel.add(timeSelectionPanel);

		createStartButton(countdownController);		

		buttonPanel.add(startButton);
		RootPanel.get("controlsContainer").add(buttonPanel);



	}


	private void createStartButton(final CountdownController countdownController) {
		//put start button on UI
		ClickHandler startButtonHandler = new ClickHandler() {
			public void onClick(ClickEvent event) {
				int startTime = Integer.parseInt(starttimeSelectionLabel.getText());
				countdownController.updateStartTime(startTime);
				countdownController.startCountdown();
			}
		};


		startButtonImageDisabled = new Image(BUTTON_IMAGE_PATH + "startDisabled.png");
		startButtonImageDisabled.setHeight("50px");
		startButtonImageDisabled.setWidth("50px");

		startButtonImage = new Image(BUTTON_IMAGE_PATH + "start.png");
		startButtonImage.setHeight("50px");
		startButtonImage.setWidth("50px");


		startButton = new Button();
		startButton.addClickHandler(startButtonHandler);
		startButton.getElement().appendChild(startButtonImage.getElement());
	}


	private Button createTimeSelectionButton (final boolean direction, Image buttonImage) {
		Button button = new Button();

		buttonImage.setHeight("30px");
		buttonImage.setWidth("30px");
		button.getElement().appendChild(buttonImage.getElement());		

		button.addClickHandler(new ClickHandler () {
			public void onClick(ClickEvent event) {
				int currentTime = Integer.parseInt(starttimeSelectionLabel.getText());

				if ((direction == IS_UP_BUTTON) && (currentTime < CountdownParameters.NUMBER_OF_GROUNDS)) {
					starttimeSelectionLabel.setText("" + (currentTime + 1));
				} else if ((direction == IS_DOWN_BUTTON) && (currentTime > 1)) {
					starttimeSelectionLabel.setText("" + (currentTime - 1));

				} else {
					//TODO: ignore click, notify user	
				}
			}
		});
		return button;
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

	/* 
	 * old code
	 	// initialize start time buttons
		starttimeSelectionButtons= new RadioButton[startTimes.length];
		for (int i = 0; i < startTimes.length; i++) {
			starttimeSelectionButtons[i] = new RadioButton("Zeit", startTimes[i] + "Minuten");
		}



		//set default time
		starttimeSelectionButtons[0].setValue(true);


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

	 */
}
