package com.dreamboxx.client.countdown;


public class CountdownParameters {



	/* Animation Canvas */

	public static final int COUNTDOWN_CANVAS_WIDTH = 1000; //px
	public static final int COUNTDOWN_CANVAS_HEIGHT = 500; //px


	/* Sprite */

	static final int SPRITE_HEIGHT = 40; //px
	static final int SPRITE_WIDTH = 40; //px

	
	/* Grounds */ 

	public static final int MAX_NUMBER_OF_GROUNDS = 9;

	public static final int GROUND_DEPTH = 5; //px
	public static final int DISTANCE_BETWEEN_GROUNDS = SPRITE_HEIGHT + 20; //px // was 40px
	public static final int PADDING_BETWEEN_GROUND_AND_SPRITE = 0; //px
	public static final int GROUND_WIDTH = 700; //px
	public static final int PADDING_SPRITE_LR = 10; //px
	public static final int GROUND_SIDESHIFT = 2 * SPRITE_WIDTH + PADDING_SPRITE_LR; //px was 100px
	public static final int PADDING_GROUNDS_TOP = 0; //px
	public static final int PADDING_GROUNDS_LEFT = 30; //px




	/* ANIMATION TIMES */ 

	public static final int TIME_MOVE_ALONG_GROUND = 6000; //milliseconds //TODO: increase to 60'000 for full minutes
	// public static final int TIME_WAIT_BEFORE_MOVE = 2000; // ms; turn around during this wait
	//public static final int TIME_WAIT_BEFORE_JUMP = 1000; //ms
	public static final int TIME_JUMP_TO_NEXT_GROUND = 1000; //ms





	private static int numberOfGrounds;
	
	public int startPosLRMove;
	public int endPosLRMove;
	public int startPosRLMove;
	public int endPosRLMove;

	public CountdownParameters() {

		startPosLRMove = PADDING_GROUNDS_LEFT + PADDING_SPRITE_LR; 
		endPosLRMove = PADDING_GROUNDS_LEFT + GROUND_WIDTH - SPRITE_WIDTH - PADDING_SPRITE_LR;
		startPosRLMove = PADDING_GROUNDS_LEFT + GROUND_SIDESHIFT + GROUND_WIDTH - SPRITE_WIDTH - PADDING_SPRITE_LR;
		endPosRLMove = PADDING_GROUNDS_LEFT + GROUND_SIDESHIFT + PADDING_SPRITE_LR;

		// old version:
//		startPosLRMove = PADDING_GROUNDS_LEFT + GROUND_SIDESHIFT / 2;
//		endPosLRMove = PADDING_GROUNDS_LEFT + GROUND_WIDTH - GROUND_SIDESHIFT / 2 ;
//		startPosRLMove = endPosLRMove + GROUND_SIDESHIFT - SPRITE_WIDTH;
//		endPosRLMove = startPosLRMove + GROUND_SIDESHIFT - SPRITE_WIDTH;

	}

	
	public static int getMaxNumberOfGrounds() {
		//TODO: compute max number of grounds from canvas height
		return MAX_NUMBER_OF_GROUNDS;
	}
	
	

	public boolean isLRmove(int groundNo) {
		if (0 == groundNo % 2) {
			// groundNo is even, e.g. 0, 2, 4 etc. 
			// move is from left to right
			return true;
		} else {
			// groundNo is odd, hence move is from right to left
			return false;
		}
	}

	public static void setNumberOfGrounds(int numberOfGrounds) {
		CountdownParameters.numberOfGrounds = numberOfGrounds;
	}
	
	public static int getNumberOfGrounds() {
		return numberOfGrounds;
	}
	
	public int getYPos(int groundNo) {
		return PADDING_GROUNDS_TOP 
				+ (getNumberOfGrounds() - groundNo) * DISTANCE_BETWEEN_GROUNDS 
				- PADDING_BETWEEN_GROUND_AND_SPRITE 
				- SPRITE_HEIGHT;	
	}

}
