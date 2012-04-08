package com.dreamboxx.client.countdown;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.animation.client.Animation;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

public class JumpAnimation extends Animation {
	
	public static final double GRAVITATION_CONSTANT = -9.81;
	public static final double JUMP_FACTOR = 7;
	
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
