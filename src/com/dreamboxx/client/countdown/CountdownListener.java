package com.dreamboxx.client.countdown;

public interface CountdownListener {
	public void updateStartTime(int startTime) ;
	public void startCountdown();
	public void finishCountdown();
}
