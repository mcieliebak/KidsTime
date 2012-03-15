package com.dreamboxx.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

public class SpriteSelection implements CountdownListener {


	private final static int NUMBER_OF_SPRITES = 2;
	private final static String FILENAME_APPENDIX_GO_LEFT = "_GO_LEFT";
	private final static String FILENAME_APPENDIX_GO_RIGHT = "_GO_RIGHT";
	private final static String FILENAME_APPENDIX_STILL = "_STILL";
	private final static String SPRITES_IMAGE_PATH = "images/sprites/";

	private final String[] spriteNames = {"Baby", "Bunny"};

	private String selectedSprite;

	public String getSpriteGoLeft() {
		return SPRITES_IMAGE_PATH + selectedSprite + FILENAME_APPENDIX_GO_LEFT + ".gif";
	}

	public String getSpriteGoRight() {
		return SPRITES_IMAGE_PATH + selectedSprite + FILENAME_APPENDIX_GO_RIGHT + ".gif";
	}
	
	public String getSpriteStill() {
		return SPRITES_IMAGE_PATH + selectedSprite + FILENAME_APPENDIX_STILL + ".gif";
	}

	public SpriteSelection(CountdownController countdownController) {
		countdownController.addCountdownListener(this);

		drawSpriteSelection();

		selectedSprite = spriteNames[0];
	}

	// TODO: change selcted sprites upon click on button

	private void drawSpriteSelection() {
		HorizontalPanel panel = new HorizontalPanel();
		panel.setWidth("100%");
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);




		// need to use com.google.gwt... since vaadin has its own Image class used in our animation
		//TODO: use import
		com.google.gwt.user.client.ui.Image[] sprites = new com.google.gwt.user.client.ui.Image[NUMBER_OF_SPRITES];
		final PushButton buttons[] = new PushButton[NUMBER_OF_SPRITES];

		ClickHandler spriteSelectionHandler = new ClickHandler () {
			public void onClick(ClickEvent event) {
				System.out.println("click on some Button");
				for (int i = 0; i < NUMBER_OF_SPRITES; i++) {
					System.out.println("event.getSource() = " + event.getSource());
					if (event.getSource().equals(buttons[i])) {
						System.out.println("event source was button i = " + i);
						selectedSprite = spriteNames[i];
						System.out.println("new selected Sprite is " + selectedSprite);
					}

				}
			}
		};

		for (int i=0; i < NUMBER_OF_SPRITES; i++) {

			String imageName = SPRITES_IMAGE_PATH + spriteNames[i] + FILENAME_APPENDIX_STILL + ".gif";
			sprites[i] = new com.google.gwt.user.client.ui.Image(imageName);
			sprites[i].setPixelSize(30,30);
			buttons[i] = new PushButton(sprites[i]);
			buttons[i].setWidth("30px");
			buttons[i].setHeight("30px");
			//buttons[i].setTitle(spriteNames[i]);
			buttons[i].setStylePrimaryName("spriteButton");
			buttons[i].addClickHandler(spriteSelectionHandler);
			panel.add(buttons[i]);
			System.out.println("SpriteSelection: sprite[" + i + "] is " + imageName);

		}

		buttons[0].setEnabled(true);
		System.out.println("SpriteSelection: Number of widgets is " + panel.getWidgetCount());
		System.out.println("Button: " + panel.getWidget(0).getStyleName());

		RootPanel.get("spritesContainer").add(panel);
	}




	@Override
	public void updateStartTime(int startTime) {
		//do nothing
	}

	@Override
	public void startCountdown() {
		//TODO: disable sprite selection
	}

	@Override
	public void finishCountdown() {
		//TODO: enable sprite selection
	}

}
