package com.dreamboxx.client.countdown;

import com.allen_sauer.gwt.log.client.Log;
import com.dreamboxx.client.SoundManager;
import com.dreamboxx.client.SpriteSelection;
import com.dreamboxx.client.countdown.MoveAlongGroundAnimation;
import com.google.gwt.media.client.Audio;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;

public class SpriteController {
	private final static String SOUNDS_PATH = "sounds/countdown/";
	private final static String SOUND_GOAL_REACHED = "fanfare";

	Audio goalSound;
	SoundManager soundManager = new SoundManager();

	private AbsolutePanel countdownPanel;

	CountdownController countdownController;
	public Image spriteMoveLeft;
	public Image spriteMoveRight;
	public Image sprite;
	private final CountdownParameters params = new CountdownParameters();
	private SpriteSelection spriteSelection;

	public SpriteController(CountdownController countdownController, AbsolutePanel countdownPanel, SpriteSelection spriteSelection) {

		this.countdownController = countdownController;
		this.countdownPanel = countdownPanel;
		this.spriteSelection = spriteSelection;
		
		Log.debug("SpriteController: countdownPanel = " + this.countdownPanel);

		Log.debug("SpriteController: soundmanager = " + soundManager);
		goalSound = soundManager.initSound(SOUNDS_PATH + SOUND_GOAL_REACHED);
		goalSound.setVolume(0.5);

		spriteMoveLeft = new Image(spriteSelection.getSpriteGoLeft());
		spriteMoveLeft.setSize(CountdownParameters.SPRITE_WIDTH + "px", CountdownParameters.SPRITE_HEIGHT + "px");

		spriteMoveRight = new Image(spriteSelection.getSpriteGoRight());
		spriteMoveRight.setSize(CountdownParameters.SPRITE_WIDTH + "px", CountdownParameters.SPRITE_HEIGHT + "px");

		sprite = new Image(spriteSelection.getSpriteStill());
		sprite.setSize(CountdownParameters.SPRITE_WIDTH + "px", CountdownParameters.SPRITE_HEIGHT + "px");


		

	}

	public void startAnimation(int ground) {
		// remove sprites from previous animations
		sprite.removeFromParent();
		// use current sprite from sprite selection
		spriteMoveLeft = new Image(spriteSelection.getSpriteGoLeft());
		spriteMoveLeft.setSize(CountdownParameters.SPRITE_WIDTH + "px", CountdownParameters.SPRITE_HEIGHT + "px");

		spriteMoveRight = new Image(spriteSelection.getSpriteGoRight());
		spriteMoveRight.setSize(CountdownParameters.SPRITE_WIDTH + "px", CountdownParameters.SPRITE_HEIGHT + "px");

		sprite = new Image(spriteSelection.getSpriteStill());
		sprite.setSize(CountdownParameters.SPRITE_WIDTH + "px", CountdownParameters.SPRITE_HEIGHT + "px");
		startMoveAlongGround(ground);


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
		countdownPanel.add(sprite, params.endPosLRMove, params.getYPos(0));

		goalSound.play();
		goalSound = soundManager.initSound(SOUNDS_PATH + SOUND_GOAL_REACHED);

		countdownController.finishCountdown();
	}

	private void startMoveToNextGround(int ground) {
		Log.debug("startMoveToNextGround started with ground " + ground);
		JumpToNextGroundAnimation anim = new JumpToNextGroundAnimation(countdownPanel, sprite);
		anim.setCallback(this);
		anim.setCurrentGround(ground);
		anim.run(CountdownParameters.TIME_JUMP_TO_NEXT_GROUND);
	}

	public void startMoveAlongGround(int ground) {
		Log.debug("startMoveAlongGround started with ground " + ground);

		MoveAlongGroundAnimation anim = new MoveAlongGroundAnimation(countdownPanel, sprite);
		anim.setCallback(this);
		anim.setCurrentGround(ground);
		anim.run(CountdownParameters.TIME_MOVE_ALONG_GROUND - CountdownParameters.TIME_JUMP_TO_NEXT_GROUND);
	}
}
