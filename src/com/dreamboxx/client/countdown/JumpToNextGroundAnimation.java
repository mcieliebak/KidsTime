package com.dreamboxx.client.countdown;

import com.allen_sauer.gwt.log.client.Log;
import com.dreamboxx.client.SoundManager;
import com.dreamboxx.client.countdown.SpriteController;
import com.google.gwt.media.client.Audio;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;



public class JumpToNextGroundAnimation extends JumpAnimation {
	private final static String SOUNDS_PATH = "sounds/countdown/";
	private final static String SOUND_GO_NEXT_GROUND = "fall";
	
	private int currentGround;
	private SpriteController controller;
	private final CountdownParameters params = new CountdownParameters();
	SoundManager soundManager = new SoundManager();

	Audio sound;
	
	public JumpToNextGroundAnimation(AbsolutePanel p, Widget w) {
		super(p, w);
		sound = soundManager.initSound(SOUNDS_PATH + SOUND_GO_NEXT_GROUND);		
		Log.debug("JumpToNextGroundAnimation created");
	}

	public void setCurrentGround(int currentGround) {
		Log.trace("currentGround = " + currentGround + ", controller = " + controller);
		this.currentGround = currentGround;
		int startY = params.getYPos(currentGround);
		int endY = params.getYPos(currentGround - 1);

		int startX;
		int endX;
		//TODO: put getX into a method in params

		if (params.isLRmove(currentGround)) {
			startX = params.endPosLRMove;
			endX = params.startPosRLMove;
			setWidget(controller.spriteMoveRight);
		}
		else {
			startX = params.endPosRLMove;
			endX = params.startPosLRMove;
			setWidget(controller.spriteMoveLeft);
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
		Log.trace("controller set to " + callback);
		this.controller = callback;
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.debug("JumpToNextGroundAnimation starts.");
		soundManager.play(sound);
		sound = soundManager.initSound(SOUNDS_PATH + SOUND_GO_NEXT_GROUND);
		
	}

	@Override
	protected void onComplete() {
		super.onComplete();
		widget.removeFromParent();
		Log.debug("JumpToNextGroundAnimation finished.");
		controller.moveToNextGroundFinished(currentGround);

	}
}


