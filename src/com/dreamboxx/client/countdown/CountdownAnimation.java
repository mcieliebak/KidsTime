package com.dreamboxx.client.countdown;


import java.util.Date;

import com.dreamboxx.client.SoundManager;
import com.dreamboxx.client.SpriteSelection;
import com.google.gwt.animation.client.Animation;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.media.client.Audio;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.allen_sauer.gwt.log.client.Log;


public class CountdownAnimation implements CountdownListener {

	
	
	private AbsolutePanel countdownPanel;
	private final CountdownParameters params = new CountdownParameters();
	private CountdownController countdownController;
	private SpriteSelection spriteSelection;

	SoundManager soundManager = new SoundManager();
	private SpriteController spriteController;
	Image spriteMoveLeft;
	Image spriteMoveRight;
	Image sprite;

	 
	public CountdownAnimation (CountdownController countdownController) {
		Log.debug("Animation constructor: countdownController is " + countdownController);
		this.countdownController = countdownController;
		countdownController.addCountdownListener(this);

		
		spriteSelection = new SpriteSelection(countdownController);		
		
		drawCountdownCanvas();
		spriteController = new SpriteController(countdownController, countdownPanel, spriteSelection);

	}

	@Override
	public void updateStartTime(int startTime) {
		//TODO: position Sprite
		CountdownParameters.setNumberOfGrounds(startTime);
	}



	@Override
	public void startCountdown() {

		// grounds are numbered starting with 0 for lowest ground
		// each ground covers exactly one minute
		// hence, starting ground is countdown-time - 1
		final int startGround = countdownController.getStartTime() - 1; // TODO: at the moment countdownTime is only in full minutes; improve to consider also seconds
		Log.debug("start countdown at " + getTime() 
				+  " with " + countdownController.getStartTime() + " minutes to go." );

		//TODO: cleanup canvas from last countdown
		countdownPanel.clear();
		drawGrounds();
		drawGoal();
		
		
		
		spriteController.startAnimation(startGround);




	}



	@Override
	public void finishCountdown() {
		//do nothing
	}

	
	private void drawCountdownCanvas() {
		//TODO: draw with GWT-rectangles
	
		countdownPanel = new AbsolutePanel();
		countdownPanel.setSize(CountdownParameters.COUNTDOWN_CANVAS_WIDTH + "px", 
				CountdownParameters.COUNTDOWN_CANVAS_HEIGHT + "px");
		countdownPanel.setStylePrimaryName("animationContainer");
		RootPanel.get("animationContainer").add(countdownPanel);
	
		
	}

	private void drawGrounds(){
	
		Canvas canvas = Canvas.createIfSupported();
		if (canvas == null) {
			//TODO: error message
			return;
		}
	
		//necessary to change size AND coordinateSpaceSize of canvas, otherwise everything is scaled
		canvas.setWidth(CountdownParameters.COUNTDOWN_CANVAS_WIDTH + "px");
		canvas.setHeight(CountdownParameters.COUNTDOWN_CANVAS_HEIGHT + "px");
		canvas.setCoordinateSpaceWidth(CountdownParameters.COUNTDOWN_CANVAS_WIDTH);
		canvas.setCoordinateSpaceHeight(CountdownParameters.COUNTDOWN_CANVAS_HEIGHT);
	
		countdownPanel.add(canvas, 0, 0);
	
		int xLeft, width, yTop, depth;
	
		for (int i = 0; i < CountdownParameters.getNumberOfGrounds(); i++) {
			if (0 == i % 2) { 
				// i is even: ground is shifted to the left
				xLeft =  CountdownParameters.PADDING_GROUNDS_LEFT;
				width = CountdownParameters.GROUND_WIDTH;
				yTop = CountdownParameters.PADDING_GROUNDS_TOP + (CountdownParameters.getNumberOfGrounds() - i) * CountdownParameters.DISTANCE_BETWEEN_GROUNDS;
				depth = CountdownParameters.GROUND_DEPTH;
	
			}
			else {
				// i is odd: ground is shifted to the right
				xLeft = CountdownParameters.PADDING_GROUNDS_LEFT + CountdownParameters.GROUND_SIDESHIFT;       
				width = CountdownParameters.GROUND_WIDTH;
				yTop = CountdownParameters.PADDING_GROUNDS_TOP + (CountdownParameters.getNumberOfGrounds() - i) * CountdownParameters.DISTANCE_BETWEEN_GROUNDS;    
				depth = CountdownParameters.GROUND_DEPTH;
	
			}
	
			Log.debug("ground i = " + i + " x = " + xLeft + " y = " + yTop + " width = " + width + " depth = " + depth); 
			drawGround(canvas.getContext2d(), xLeft, yTop, width, depth, 1); 
	
		}
	}

	private void drawGround(Context2d context, double x, double y, double w, double h, double r){
	
		//ground is a rectangle with rounded corners
		context.beginPath();
		context.moveTo(x+r, y);
		context.lineTo(x+w-r, y);
		context.quadraticCurveTo(x+w, y, x+w, y+r);
		context.lineTo(x+w, y+h-r);
		context.quadraticCurveTo(x+w, y+h, x+w-r, y+h);
		context.lineTo(x+r, y+h);
		context.quadraticCurveTo(x, y+h, x, y+h-r);
		context.lineTo(x, y+r);
		context.quadraticCurveTo(x, y, x+r, y);    
	
		context.setStrokeStyle("blue");
		context.stroke();         
		context.setFillStyle("f0ffff");
		context.fill();    
	
	
	}



	private void drawGoal() {
		Image goalImage = new Image("images/goal.png");
		goalImage.setSize("100px", "100px");
		int x = params.endPosLRMove + 50;
		int y = params.getYPos(0);
		countdownPanel.add(goalImage, x, y);
		Log.debug("goal is added to panel at x = " + x + " y = " + y);
	}




	private String getTime() {
		Date date = new Date();
		DateTimeFormat dtf = DateTimeFormat.getFormat("dd.MM.yyyy (E) - HH:mm:ss:SS");
		return(dtf.format(date, TimeZone.createTimeZone(-60)));
	}



}
