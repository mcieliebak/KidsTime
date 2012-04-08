package com.dreamboxx.client.countdown;

import com.allen_sauer.gwt.log.client.Log;
import com.dreamboxx.client.countdown.HorizontalMove;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

public class MoveAlongGroundAnimation extends HorizontalMove {
	private final CountdownParameters params = new CountdownParameters();
	private int currentGround;
	private SpriteController controller;

	public MoveAlongGroundAnimation(AbsolutePanel p, Widget w) {
		super(p, w);
		System.out.println("MoveAlongGroundAnimation created");

	}

	public void setCurrentGround(int currentGround) {
		this.currentGround = currentGround;
		int startX;
		int endX;

		Log.trace("setCurrentGround with ground = " + currentGround);
		Log.trace("params.getY(currentGround) = " + params.getYPos(currentGround));
		
		if (params.isLRmove(currentGround)) {
			Log.trace("MoveAlongGroundAnimation: change sprite to " + controller.spriteMoveRight );
			Log.trace("panel = " + panel);
			startX =  params.startPosLRMove;
			endX = params.endPosLRMove;
			setWidget(controller.spriteMoveRight);
			Log.trace("new widget is" + widget);
			panel.add(widget, startX, params.getYPos(currentGround));

		}
		else {
			Log.trace("MoveAlongGroundAnimation: change sprite to " + controller.spriteMoveLeft);
			startX = params.startPosRLMove;
			endX = params.endPosRLMove;
			setWidget(controller.spriteMoveLeft);
			Log.trace("new widget is" + widget);
			panel.add(widget, startX, params.getYPos(currentGround));

		}
		super.setParams(startX, endX);

		//add image if not already there
		System.out.println("MoveAlongGroundAnimation: parameters have been set to currentGround = " + currentGround + " startX = " + startX + " endX = " + endX + ".");

	}

	public void setCallback(SpriteController callback) {
		this.controller = callback;
	}

	@Override
	protected void onComplete() {
		super.onComplete();
		widget.removeFromParent();
		controller.moveAlongGroundFinished(currentGround);
	}
}
