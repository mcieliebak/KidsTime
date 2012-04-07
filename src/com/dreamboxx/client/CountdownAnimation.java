package com.dreamboxx.client;


import java.util.Date;

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

	private final static String SOUNDS_PATH = "sounds/countdown/";
	private final static String SOUND_GO_NEXT_GROUND = "fall";
	private final static String SOUND_GOAL_REACHED = "fanfare";
	
	
	private AbsolutePanel countdownPanel;
	private final CountdownParameters params = new CountdownParameters();
	private CountdownController countdownController;
	private SpriteSelection spriteSelection;

	SoundManager soundManager = new SoundManager();
	private SpriteController spriteController = new SpriteController();
	Image spriteMoveLeft;
	Image spriteMoveRight;
	Image sprite;


	    
	private class JumpAnimation extends Animation {
		
		public static final double GRAVITATION_CONSTANT = -9.81;
		public static final double JUMP_FACTOR = 5;
		
			AbsolutePanel panel;
			Widget widget;
			int startPosX = 0;
			int startPosY = 0;
			int endPosX = 0;
			int endPosY = 0;
			double initialSpeedY = 0;
			
			
			
			public JumpAnimation(AbsolutePanel p, Widget w) {
				super();
				this.panel = p;
				this.widget = w;		
			}
		
			public void setWidget(Widget w) {
				this.widget = w;
			}
		
			public void setParams(int startPosX, int startPosY, int endPosX, int endPosY) {
				this.startPosX = startPosX; 
				this.startPosY = startPosY;
				this.endPosX = endPosX;
				this.endPosY = endPosY;
				
				initialSpeedY = (endPosY - startPosY + GRAVITATION_CONSTANT / 2.0 * JUMP_FACTOR * JUMP_FACTOR) / JUMP_FACTOR;
			}
		
			@Override
			protected void onStart() {
				super.onStart();
				Log.debug("Jump from (" + startPosX + ", " + startPosY + ") to (" + endPosX + ", " + endPosY + ") with speed " + initialSpeedY + " starts");
			}
		
			@Override
			protected void onComplete() {
				super.onComplete();
				Log.debug("Jump completed."); 
			}
		
			@Override
			protected void onUpdate(double progress) {
				
				
				// horizontal movement is uniform from start to end
				int x = (int) (progress * (endPosX - startPosX) + startPosX); 
	
				// vertical movement
				double t = progress * JUMP_FACTOR;
				int y = (int) (startPosY + initialSpeedY * t 
						- GRAVITATION_CONSTANT / 2.0 * t * t);

				panel.setWidgetPosition(widget, x , y);
	
			}
		}


	
	public class JumpToNextGroundAnimation extends JumpAnimation {
		private int currentGround;
		private SpriteController callback;
	
		Audio sound;
		
		public JumpToNextGroundAnimation() {
			super(countdownPanel, sprite);
			sound = soundManager.initSound(SOUNDS_PATH + SOUND_GO_NEXT_GROUND);		
		}
	
		public void setCurrentGround(int currentGround) {
			this.currentGround = currentGround;
			int startY = params.getY(currentGround);
			int endY = params.getY(currentGround - 1);
	
			int startX;
			int endX;
			//TODO: put getX into a method in params

			if (params.isLRmove(currentGround)) {
				startX = params.endLRMove;
				endX = params.startRLMove;
				setWidget(spriteMoveRight);
			}
			else {
				startX = params.endRLMove;
				endX = params.startLRMove;
				setWidget(spriteMoveLeft);
			}
	
			panel.add(widget, startX, startY);
			super.setParams(startX, startY, endX, endY);
			Log.debug("Jump parameters set to: " 
					+ "startX = " + startX 
					+ ", startY = " + startY 
					+ ", endX = " + endX
					+ ", endY = " + endY + ".");
		}
	
		public void setCallback(SpriteController callback) {
			this.callback = callback;
		}
		
		
		@Override
		protected void onStart() {
			super.onStart();
			soundManager.play(sound);
			sound = soundManager.initSound(SOUNDS_PATH + SOUND_GO_NEXT_GROUND);
			Log.debug("JumpToNextGroundAnimation starts");
		}
	
		@Override
		protected void onComplete() {
			super.onComplete();
			widget.removeFromParent();
			callback.moveToNextGroundFinished(currentGround);
	
		}
	}
	
	
	
	
	
	
	
	
	
	private class SpriteController {
		Audio goalSound;
	
		public SpriteController() {
			Log.debug("SpriteController: soundmanager = " + soundManager);
			goalSound = soundManager.initSound(SOUNDS_PATH + SOUND_GOAL_REACHED);
			goalSound.setVolume(0.5);
		}
		
		public void moveAlongGroundFinished (int ground) {
			Log.debug("SpriteController: moveAlongGroundFinished with ground " + ground);
			if (0 == ground) {
				//last ground is finished
				startGoalAnimation();
			} else {
				startMoveToNextGround(ground);
			}
		}
	
		public void moveToNextGroundFinished(int ground) {
			Log.debug("SpriteController: moveToNextGroundFinished with ground " + ground);
			startMoveAlongGround(ground - 1);
		}
		
		
		private void startGoalAnimation() {
			
			//sprite was removed at end of Animation, hence, show again
			//TODO: have "dancing" sprite here
			countdownPanel.add(sprite, params.endLRMove, params.getY(0));
			
			goalSound.play();
			goalSound = soundManager.initSound(SOUNDS_PATH + SOUND_GOAL_REACHED);
	
			countdownController.finishCountdown();
		}
		
		private void startMoveToNextGround(int ground) {
			Log.debug("startMoveToNextGround started with ground " + ground);
			JumpToNextGroundAnimation anim = new JumpToNextGroundAnimation();
			anim.setCurrentGround(ground);
			anim.setCallback(this);
			anim.run(CountdownParameters.TIME_JUMP_TO_NEXT_GROUND);
		}
		
		public void startMoveAlongGround(int ground) {
			Log.debug("startMoveAlongGround started with ground " + ground);
	
			MoveAlongGroundAnimation anim = new MoveAlongGroundAnimation();
			anim.setCallback(this);
			anim.setCurrentGround(ground);
			anim.run(CountdownParameters.TIME_MOVE_ALONG_GROUND - CountdownParameters.TIME_JUMP_TO_NEXT_GROUND);
		}
	}

	


	public CountdownAnimation (CountdownController countdownController) {
		Log.debug("Animation constructor: countdownController is " + countdownController);
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

	@Override
	public void updateStartTime(int startTime) {
		//TODO: position Sprite
	
	}



	@Override
	public void startCountdown() {

		// grounds are numbered starting with 0 for lowest ground
		// each ground covers exactly one minute
		// hence, starting ground is countdown-time - 1
		final int startGround = countdownController.getStartTime() - 1; // TODO: at the moment countdownTime is only in full minutes; improve to consider also seconds
		Log.debug("start countdown at " + getTime() 
				+  " with " + countdownController.getStartTime() + " minutes to go." );


		// remove sprites from previous animations
		sprite.removeFromParent();
		
		// use current sprite from sprite selection
		spriteMoveLeft = new Image(spriteSelection.getSpriteGoLeft());
		spriteMoveLeft.setSize(CountdownParameters.SPRITE_WIDTH + "px", CountdownParameters.SPRITE_HEIGHT + "px");

		spriteMoveRight = new Image(spriteSelection.getSpriteGoRight());
		spriteMoveRight.setSize(CountdownParameters.SPRITE_WIDTH + "px", CountdownParameters.SPRITE_HEIGHT + "px");

		sprite = new Image(spriteSelection.getSpriteStill());
		sprite.setSize(CountdownParameters.SPRITE_WIDTH + "px", CountdownParameters.SPRITE_HEIGHT + "px");

		
		spriteController.startMoveAlongGround(startGround);




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
	
		drawGrounds();
		drawGoal();
	
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
		int x = params.endLRMove + 50;
		int y = params.getY(0);
		countdownPanel.add(goalImage, x, y);
		Log.debug("goal is added to panel at x = " + x + " y = " + y);
	}




	private String getTime() {
		Date date = new Date();
		DateTimeFormat dtf = DateTimeFormat.getFormat("dd.MM.yyyy (E) - HH:mm:ss:SS");
		return(dtf.format(date, TimeZone.createTimeZone(-60)));
	}

//	private class SpriteController {
//		Audio goalSound;
//	
//		public SpriteController() {
//			System.out.println("SpriteController: soundmanager = " + soundManager);
//			goalSound = soundManager.initSound(SOUNDS_PATH + SOUND_GOAL_REACHED);
//			goalSound.setVolume(0.5);
//		}
//		
//		public void moveAlongGroundFinished (int ground) {
//			System.out.println("SpriteController: moveAlongGroundFinished with ground " + ground);
//			if (0 == ground) {
//				//last ground is finished
//				startGoalAnimation();
//			} else {
//				startMoveToNextGround(ground);
//			}
//		}
//	
//		public void moveToNextGroundFinished(int ground) {
//			System.out.println("SpriteController: moveToNextGroundFinished with ground " + ground);
//			startMoveAlongGround(ground - 1);
//		}
//		
//		
//		private void startGoalAnimation() {
//			//Window.alert("Hurray!!");
//			
//			//sprite was removed at end of Animation, hence, show again
//			//TODO: have "dancing" sprite here
//			countdownPanel.add(sprite, params.endLRMove, params.getY(0));
//			
//			goalSound.play();
//			goalSound = soundManager.initSound(SOUNDS_PATH + SOUND_GOAL_REACHED);
//	
//			countdownController.finishCountdown();
//		}
//		
//		private void startMoveToNextGround(int ground) {
//			System.out.println("startMoveToNextGround started with ground " + ground);
//			MoveToNextGroundAnimation anim = new MoveToNextGroundAnimation();
//			anim.setCurrentGround(ground);
//			anim.setCallback(this);
//			anim.run(CountdownParameters.TIME_JUMP_TO_NEXT_GROUND);
//		}
//		
//		public void startMoveAlongGround(int ground) {
//			System.out.println("startMoveAlongGround started with ground " + ground);
//	
//			MoveAlongGroundAnimation anim = new MoveAlongGroundAnimation();
//			anim.setCallback(this);
//			anim.setCurrentGround(ground);
//			anim.run(CountdownParameters.TIME_MOVE_ALONG_GROUND - CountdownParameters.TIME_JUMP_TO_NEXT_GROUND);
//		}
//	}
//
//	private class MoveToNextGroundAnimation extends VerticalMove {
//		private int currentGround;
//		private SpriteController callback;
//	
//		Audio sound;
//		
//		public MoveToNextGroundAnimation() {
//			super(countdownPanel, sprite);
//			sound = soundManager.initSound(SOUNDS_PATH + SOUND_GO_NEXT_GROUND);
//			System.out.println("MoveToNextGroundAnimation created");
//		}
//	
//		public void setCurrentGround(int currentGround) {
//			this.currentGround = currentGround;
//			int startY = params.getY(currentGround);
//			int endY = params.getY(currentGround - 1);
//	
//			int x;
//			//TODO: put getX into a method in params
//			if (params.isLRmove(currentGround)) {
//				x = params.endLRMove;
//				setWidget(spriteMoveRight);
//			}
//			else {
//				x = params.endRLMove;
//				setWidget(spriteMoveLeft);
//			}
//	
//			panel.add(widget, x, startY);
//			super.setParams(startY, endY);
//		}
//	
//		public void setCallback(SpriteController callback) {
//			this.callback = callback;
//		}
//		
//		
//		@Override
//		protected void onStart() {
//			super.onStart();
//			soundManager.play(sound);
//			sound = soundManager.initSound(SOUNDS_PATH + SOUND_GO_NEXT_GROUND);
//			
//		}
//	
//		@Override
//		protected void onComplete() {
//			super.onComplete();
//			widget.removeFromParent();
//			callback.moveToNextGroundFinished(currentGround);
//	
//		}
//	}
//
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

//	private class VerticalMove extends Animation {
//	
//		AbsolutePanel panel;
//		Widget widget;
//		int startPos = 0;
//		int endPos = 0;
//		
//		
//		public VerticalMove(AbsolutePanel p, Widget w) {
//			super();
//			this.panel = p;
//			this.widget = w;
//		}
//	
//		public void setParams(int startPos, int endPos) {
//			this.startPos = startPos; 
//			this.endPos = endPos;
//		}
//	
//		public void setWidget(Widget w) {
//			this.widget = w;
//		}
//		@Override
//		protected void onStart() {
//			super.onStart();
//			System.out.println("Vertical move of widget " + widget + " from " + startPos 
//					+ " to " + endPos + " starts.");
//		}
//	
//		@Override
//		protected void onComplete() {
//			super.onComplete();
//			System.out.println("Vertical move of widget " + widget + "completed."); 
//		}
//	
//		@Override
//		protected void onUpdate(double progress) {
//			int x = panel.getWidgetLeft(widget);
//			int y = (int) (progress * (endPos - startPos) + startPos);
//			panel.setWidgetPosition(widget, x , y);
//		}
//	}
//

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
			Log.debug("Horizontal move of widget " + widget + " from " + startPos 
					+ " to " + endPos + " starts.");
		}
	
		@Override
		protected void onComplete() {
			super.onComplete();
			Log.debug("Horizontal move of widget " + widget + "completed."); 
		}
	
		@Override
		protected void onUpdate(double progress) {
			int x = (int) (progress * (endPos - startPos) + startPos);
			int y = panel.getWidgetTop(widget);
			panel.setWidgetPosition(widget, x , y);
		}
	}

}
