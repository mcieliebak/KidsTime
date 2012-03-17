package com.dreamboxx.client;


import java.util.Date;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


import org.vaadin.gwtgraphics.client.animation.Animate;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.google.gwt.animation.client.Animation;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;



public class NewCountdownAnimation implements CountdownListener {


	private AbsolutePanel countdownPanel;
	private final CountdownParameters params = new CountdownParameters();
	private CountdownController countdownController;
	private SpriteSelection spriteSelection;

	Image spriteMoveLeft;
	Image spriteMoveRight;
	Image sprite;




	public NewCountdownAnimation (CountdownController countdownController) {
		System.out.println("Animation constructor: countdownController is " + countdownController);
		this.countdownController = countdownController;
		countdownController.addCountdownListener(this);

		spriteSelection = new SpriteSelection(countdownController);		

		spriteMoveLeft = new Image(spriteSelection.getSpriteGoLeft());
		spriteMoveLeft.setSize(CountdownParameters.SPRITE_WIDTH + "px", CountdownParameters.SPRITE_HEIGHT + "px");

		spriteMoveRight = new Image(spriteSelection.getSpriteGoRight());
		spriteMoveRight.setSize(CountdownParameters.SPRITE_WIDTH + "px", CountdownParameters.SPRITE_HEIGHT + "px");

		sprite = new Image(spriteSelection.getSpriteStill());
		sprite.setSize(CountdownParameters.SPRITE_WIDTH + "px", CountdownParameters.SPRITE_HEIGHT + "px");

		drawCountdownCanvas();

	}

	private void drawCountdownCanvas() {
		//TODO: draw with GWT-rectangles

		countdownPanel = new AbsolutePanel();
		countdownPanel.setSize(CountdownParameters.COUNTDOWN_CANVAS_WIDTH + "px", 
				CountdownParameters.COUNTDOWN_CANVAS_HEIGHT + "px");
		countdownPanel.setStylePrimaryName("animationContainer");
		RootPanel.get("animationContainer").add(countdownPanel);

		drawGrounds();
		drawGoal();

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

			System.out.println("ground i = " + i + " x = " + xLeft + " y = " + yTop + " width = " + width + " depth = " + depth); 
			drawGround(canvas.getContext2d(), xLeft, yTop, width, depth, 1); 

		}
	}




	private class VerticalMove extends Animation {

		AbsolutePanel panel;
		Widget widget;
		int startPos = 0;
		int endPos = 0;

		public VerticalMove(AbsolutePanel p, Widget w) {
			super();
			this.panel = p;
			this.widget = w;
		}

		public void setParams(int startPos, int endPos) {
			this.startPos = startPos; 
			this.endPos = endPos;
		}

		public void setWidget(Widget w) {
			this.widget = w;
		}
		@Override
		protected void onStart() {
			super.onStart();
			System.out.println("Vertical move of widget " + widget + " from " + startPos 
					+ " to " + endPos + " starts.");
		}

		@Override
		protected void onComplete() {
			super.onComplete();
			System.out.println("Vertical move of widget " + widget + "completed."); 
		}

		@Override
		protected void onUpdate(double progress) {
			//TODO: where to get parameters?
			int x = panel.getWidgetLeft(widget);
			int y = (int) (progress * (endPos - startPos) + startPos);
			panel.setWidgetPosition(widget, x , y);
		}
	}


	private class HorizontalMove extends Animation {

		AbsolutePanel panel;
		Widget widget;
		int startPos = 0;
		int endPos = 0;

		public HorizontalMove(AbsolutePanel p, Widget w) {
			super();
			this.panel = p;
			this.widget = w;
		}

		public void setWidget(Widget w) {
			this.widget = w;
		}

		public void setParams(int startPos, int endPos) {
			this.startPos = startPos; 
			this.endPos = endPos;
		}

		@Override
		protected void onStart() {
			super.onStart();
			System.out.println("Horizontal Move start");
			System.out.println("Horizontal move of widget " + widget + " from " + startPos 
					+ " to " + endPos + " starts.");
		}

		@Override
		protected void onComplete() {
			super.onComplete();
			System.out.println("Horizontal move of widget " + widget + "completed."); 
		}

		@Override
		protected void onUpdate(double progress) {
			//TODO: where to get parameters?
			//			System.out.println("Horizontal Move: update with progress = " + progress);
			//			System.out.println("panel = " + panel);
			//			System.out.println("widget = " + widget);
			int x = (int) (progress * (endPos - startPos) + startPos);
			int y = panel.getWidgetTop(widget);
			//			System.out.println("x = " + x);
			//			System.out.println("y = " + y);
			panel.setWidgetPosition(widget, x , y);
			//			System.out.println("Horizontal Move: update done.");

		}
	}

	private class SpriteController {

		public void moveAlongGroundFinished (int ground) {
			System.out.println("SpriteController: moveAlongGroundFinished with ground " + ground);
			if (0 == ground) {
				//last ground is finished
				startGoalAnimation();
			} else {
				startMoveToNextGround(ground);
			}
		}

		public void moveToNextGroundFinished(int ground) {
			System.out.println("SpriteController: moveToNextGroundFinished with ground " + ground);
			startMoveAlongGround(ground - 1);
		}
		private void startGoalAnimation() {
			//Window.alert("Hurray!!");
			
			//sprite was removed at end of Animation, hence, show again
			//TODO: have "dancing" sprite here
			countdownPanel.add(sprite, params.endLRMove, params.getY(0));
			
			SoundController soundController = new SoundController();
			Sound sound = soundController.createSound(Sound.MIME_TYPE_AUDIO_WAV_UNKNOWN,
					"sounds/fanfare.wav");
			sound.play();
			countdownController.finishCountdown();
		}
		private void startMoveToNextGround(int ground) {
			System.out.println("startMoveToNextGround started with ground " + ground);
			MoveToNextGroundAnimation anim = new MoveToNextGroundAnimation();
			anim.setCurrentGround(ground);
			anim.setCallback(this);
			anim.run(CountdownParameters.TIME_MOVE_TO_NEXT_GROUND);
		}
		public void startMoveAlongGround(int ground) {
			System.out.println("startMoveAlongGround started with ground " + ground);

			MoveAlongGroundAnimation anim = new MoveAlongGroundAnimation();
			anim.setCallback(this);
			anim.setCurrentGround(ground);
			anim.run(CountdownParameters.TIME_MOVE_TO_NEXT_GROUND);
		}
	}

	private SpriteController spriteController = new SpriteController();


	private class MoveToNextGroundAnimation extends VerticalMove {
		private int currentGround;
		private SpriteController callback;

		public MoveToNextGroundAnimation() {
			super(countdownPanel, sprite);
			System.out.println("MoveToNextGroundAnimation created");
		}

		public void setCurrentGround(int currentGround) {
			this.currentGround = currentGround;
			int startY = params.getY(currentGround);
			int endY = params.getY(currentGround - 1);

			int x;
			//TODO: put getX into a method in params
			if (params.isLRmove(currentGround)) {
				x = params.endLRMove;
				setWidget(spriteMoveRight);
			}
			else {
				x = params.endRLMove;
				setWidget(spriteMoveLeft);
			}

			panel.add(widget, x, startY);
			super.setParams(startY, endY);
		}

		public void setCallback(SpriteController callback) {
			this.callback = callback;
		}
		
		
		@Override
		protected void onStart() {
			super.onStart();
			SoundController soundController = new SoundController();
			Sound sound = soundController.createSound(Sound.MIME_TYPE_AUDIO_WAV_UNKNOWN,
					"sounds/pieww_jump.wav");

			sound.play();
		}

		@Override
		protected void onComplete() {
			super.onComplete();
			widget.removeFromParent();
			callback.moveToNextGroundFinished(currentGround);

		}
	}

	private class MoveAlongGroundAnimation extends HorizontalMove {
		private int currentGround;
		private SpriteController callback;

		public MoveAlongGroundAnimation() {
			super(countdownPanel, sprite);
			System.out.println("MoveAlongGroundAnimation created");

		}

		public void setCurrentGround(int currentGround) {
			this.currentGround = currentGround;
			int startX;
			int endX;

			if (params.isLRmove(currentGround)) {
				startX =  params.startLRMove;
				endX = params.endLRMove;
				setWidget(spriteMoveRight);
				panel.add(widget, startX, params.getY(currentGround));
				System.out.println("MoveAlongGroundAnimation:  changed sprite to " + spriteMoveRight );
				System.out.println("new widget is" + widget);

			}
			else {
				startX = params.startRLMove;
				endX = params.endRLMove;
				setWidget(spriteMoveLeft);
				panel.add(widget, startX, params.getY(currentGround));
				System.out.println("MoveAlongGroundAnimation:  changed sprite to " + spriteMoveLeft);
				System.out.println("new widget is" + widget);

			}
			super.setParams(startX, endX);

			//add image if not already there
			System.out.println("MoveAlongGroundAnimation: parameters have been set to currentGround = " + currentGround + " startX = " + startX + " endX = " + endX + ".");

		}

		public void setCallback(SpriteController callback) {
			this.callback = callback;
		}

		@Override
		protected void onComplete() {
			super.onComplete();
			widget.removeFromParent();
			callback.moveAlongGroundFinished(currentGround);
		}
	}


	private void drawGoal() {
		Image goalImage = new Image("images/goal.png");
		goalImage.setSize("100px", "100px");
		int x = params.endLRMove + 50;
		int y = params.getY(0);
		countdownPanel.add(goalImage, x, y);
		System.out.println("goal is added to panel at x = " + x + " y = " + y);
	}


	@Override
	public void startCountdown() {

		// grounds are numbered starting with 0 for lowest ground
		// each ground covers exactly one minute
		// hence, starting ground is countdown-time - 1
		final int startGround = countdownController.getStartTime() - 1; // TODO: at the moment countdownTime is only in full minutes; improve to consider also seconds
		System.out.println("start countdown at " + getTime() 
				+  " with " + countdownController.getStartTime() + " minutes to go." );

		spriteController.startMoveAlongGround(startGround);




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