package com.dreamboxx.client;


import java.util.Date;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


import org.vaadin.gwtgraphics.client.*;
import org.vaadin.gwtgraphics.client.animation.Animate;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.ui.RootPanel;



public class CountdownAnimation implements CountdownListener {


	//	Logger logger = LoggerFactory.getLogger("CountdownAnimation");


	private DrawingArea countdownCanvas;
	private final CountdownParameters params = new CountdownParameters();
	private CountdownController countdownController;
	private SpriteSelection spriteSelection;






	public CountdownAnimation (CountdownController countdownController) {
		System.out.println("Animation constructor: countdownController is " + countdownController);
		this.countdownController = countdownController;
		countdownController.addCountdownListener(this);


		drawCountdownCanvas();

		spriteSelection = new SpriteSelection(countdownController);		


	}

	private void drawCountdownCanvas() {

		countdownCanvas = new DrawingArea(CountdownParameters.COUNTDOWN_CANVAS_WIDTH, CountdownParameters.COUNTDOWN_CANVAS_HEIGHT);
		countdownCanvas.setStylePrimaryName("animationContainer");
		RootPanel.get("animationContainer").add(countdownCanvas);

		drawGrounds();
		drawGoal();

	}


	private void drawGrounds(){

		Rectangle ground;
		int xLeft, width, yTop, depth;

		for (int i = 0; i < CountdownParameters.NUMBER_OF_GROUNDS; i++) {
			if (0 == i % 2) { 
				// i is even: ground is shifted to the left
				xLeft =  CountdownParameters.PADDING_GROUNDS_LEFT;
				width = CountdownParameters.GROUND_WIDTH;
				yTop = CountdownParameters.PADDING_GROUNDS_TOP + (CountdownParameters.NUMBER_OF_GROUNDS - i) * CountdownParameters.DISTANCE_BETWEEN_GROUNDS;
				depth = CountdownParameters.GROUND_DEPTH;

			}
			else {
				// i is odd: ground is shifted to the right
				xLeft = CountdownParameters.PADDING_GROUNDS_LEFT + CountdownParameters.GROUND_SIDESHIFT;       
				width = CountdownParameters.GROUND_WIDTH;
				yTop = CountdownParameters.PADDING_GROUNDS_TOP + (CountdownParameters.NUMBER_OF_GROUNDS - i) * CountdownParameters.DISTANCE_BETWEEN_GROUNDS;    
				depth = CountdownParameters.GROUND_DEPTH;

			}

			ground = new Rectangle(xLeft, yTop, width, depth); 

			ground.setStrokeWidth(2);
			ground.setStrokeColor("blue");
			ground.setFillColor("darkblue");
			ground.setRoundedCorners(2);

			countdownCanvas.add(ground);
		}
	}

	private void drawGoal() {
		// TODO: do this
	}


	@Override
	public void startCountdown() {
		//	logger.debug("countdown start");

		// grounds are numbered starting with 0 for lowest ground
		// each ground covers exactly one minute
		// hence, starting ground is countdown-time - 1
		final int startGround = countdownController.getStartTime() - 1; // TODO: at the moment countdownTime is only in full minutes; improve to consider also seconds
		int startX;
		int y;

		System.out.println("start countdown at " + getTime() 
				+  " with " + countdownController.getStartTime() + " minutes to go." );

		if (params.isLRmove(startGround)) { 
			startX =  params.startLRMove;
		}
		else {
			startX = params.startRLMove;
		}

		y = params.getY(startGround);

//		final Image sprite = new Image(startX, y, 50, 70, spriteSelection.getSpriteStill());
		//countdownCanvas.add(sprite);

		RepeatingCommand myCommand = new RepeatingCommand() {

			int startX;
			int endX;
			int currentGround = startGround;
			int y;

			Image movingSprite[] = new Image[CountdownParameters.NUMBER_OF_GROUNDS + 1];

			public boolean execute () {
				System.out.println("EXECUTE starts at " + getTime());

				if (currentGround < startGround) {
					System.out.println("already in ground " + currentGround + ", hence remove previous sprite at " + getTime());
					movingSprite[currentGround+1].setVisible(false);
				}

				//countdownCanvas.remove(movingSprite);
				// position sprite on starting position on currentGround



				y = params.getY(currentGround);
				if (params.isLRmove(currentGround)) {
					startX =  params.startLRMove;
					endX = params.endLRMove;
					movingSprite[currentGround] = new Image(startX, y, CountdownParameters.SPRITE_WIDTH, CountdownParameters.SPRITE_HEIGHT, spriteSelection.getSpriteGoRight());
				}
				else {
					startX = params.startRLMove;
					endX = params.endRLMove;
					movingSprite[currentGround] = new Image(startX, y, CountdownParameters.SPRITE_WIDTH, CountdownParameters.SPRITE_HEIGHT, spriteSelection.getSpriteGoLeft());
				}

				System.out.println("currentGround = " + currentGround + ", COORD of new sprite: " + movingSprite[currentGround].getX());
				
				countdownCanvas.add(movingSprite[currentGround]);
				System.out.println("After add: number of objects in Canvas: " + countdownCanvas.getVectorObjectCount());



				if (currentGround > 0) {
					moveSpriteAlongLine(movingSprite[currentGround], startX, endX);
					return true; //moves on to next ground
				} else {
					moveSpriteAlongLine(movingSprite[currentGround], startX, endX);
					countdownController.finishCountdown();
					return false; //no more next grounds
				}


			}

			private void moveSpriteAlongLine(Image sprite, int fromX, int toX) {
				System.out.println("moveSpriteAlongLine starts");
				//				logger.debug(": y = " + sprite.getY());
				//
				//				if (fromX < toX) {
				//					logger.debug("moveSpriteAlongLine: move from left to right along line " + currentGround + ", fromX = " + fromX + ", toX = " + toX);
				//				} else {
				//					logger.debug("moveSpriteAlongLine: move from right to left along line " + currentGround + ", fromX = " + fromX + ", toX = " + toX);
				//				}

				Animate movement;
				movement = new Animate(sprite, "x", fromX, toX,  CountdownParameters.TIME_PER_GROUND); 
				movement.start();

				// go to next ground
				currentGround--;


			}
		};

		// execute command once explicitly because scheduled command only starts AFTER initial wait period
		System.out.println("before call of first execute: " + getTime());

		myCommand.execute(); 
		//countdownCanvas.remove(sprite);

		if (startGround > 0) { 
			Scheduler.get().scheduleFixedPeriod(myCommand, CountdownParameters.TIME_PER_GROUND);
		} 
		System.out.println("scheduler finished at " + getTime());
		System.out.println("nUmber of objects in Canvas: " + countdownCanvas.getVectorObjectCount());

//		Scheduler.get().scheduleFixedPeriod(new RepeatingCommand() {
//		public boolean execute() {
//			System.out.println("in Remove Scheduler, currentGround = " + currentGround);
//			
//			System.out.println("movingSprite[currentGround] removed, now number of objects in Canvas: " + countdownCanvas.getVectorObjectCount());
//			return false;
//		}
//	}, CountdownParameters.TIME_PER_GROUND);

	}



	private String getTime() {
		Date date = new Date();
		DateTimeFormat dtf = DateTimeFormat.getFormat("dd.MM.yyyy (E) - HH:mm:ss:SS");
		return(dtf.format(date, TimeZone.createTimeZone(-60)));
	}


	@Override
	public void updateStartTime(int startTime) {
		//TODO: position Sprite

	}


	@Override
	public void finishCountdown() {
		//do nothing
	}

}
