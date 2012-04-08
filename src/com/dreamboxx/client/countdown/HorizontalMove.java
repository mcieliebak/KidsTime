package com.dreamboxx.client.countdown;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.animation.client.Animation;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

public class HorizontalMove extends Animation {
	
	AbsolutePanel panel;
	Widget widget;
	int startPos = 0;
	int endPos = 0;

	public HorizontalMove(AbsolutePanel p, Widget w) {
		super();
		this.panel = p;
		this.widget = w;
		Log.debug("panel is set to " + this.panel);
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

