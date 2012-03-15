
public class SpriteSelection implements CountdownListener {

	
	public final static int NUMBER_OF_SPRITES = 1;

	public SpriteSelection(CountdownController countdownController) {
		countdownController.addCountdownListener(this);
		
		drawSpriteSelection();
	}
	
	
	private void drawSpriteSelection() {
		HorizontalPanel panel = new HorizontalPanel();
		//panel.setStyleName("demo-buttons");
		panel.setWidth("100%");
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);


		// need to use com.google.gwt... since vaadin has its own Image class used in our animation
		//TODO: use import
		com.google.gwt.user.client.ui.Image[] sprites = new com.google.gwt.user.client.ui.Image[NUMBER_OF_SPRITES];
		PushButton buttons[] = new PushButton[NUMBER_OF_SPRITES];

		for (int i=0; i < NUMBER_OF_SPRITES; i++) {
			//TODO: select different images here for other sprites
			sprites[i] = new com.google.gwt.user.client.ui.Image("images/bunny/Bunny_hopps_frame_000" + (i+1) + ".gif");
			sprites[i].setPixelSize(30,30);
			buttons[i] = new PushButton(sprites[i]);
			buttons[i].setWidth("30px");
			buttons[i].setHeight("30px");
			buttons[i].setStylePrimaryName("spriteButton");
			panel.add(buttons[i]);

		}

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
