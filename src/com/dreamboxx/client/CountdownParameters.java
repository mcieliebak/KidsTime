package com.dreamboxx.client;


public class CountdownParameters {



	/* Animation Canvas */
	
	public static final int COUNTDOWN_CANVAS_WIDTH = 1000; //px
	public static final int COUNTDOWN_CANVAS_HEIGHT = 500; //px


	/* Grounds */ 
	
	public static final int NUMBER_OF_GROUNDS = 10;

	public static final int GROUND_DEPTH = 5; //px
	public static final int DISTANCE_BETWEEN_GROUNDS = 40; //px
	public static final int PADDING_BETWEEN_GROUND_AND_SPRITE = 0; //px
	public static final int GROUND_WIDTH = 700; //px
	public static final int GROUND_SIDESHIFT = 100; //px
	public static final int PADDING_GROUNDS_TOP = 0; //px
	public static final int PADDING_GROUNDS_LEFT = 30; //px

	
	/* Sprite */
	
	static final int SPRITE_HEIGHT = 40; //px
	static final int SPRITE_WIDTH = 40; //px


	/* ANIMATION TIMES */ 

	public static final int TIME_MOVE_ALONG_GROUND = 6000; //milliseconds //TODO: increase to 60'000 for full minutes
	// public static final int TIME_WAIT_BEFORE_MOVE = 2000; // ms; turn around during this wait
	//public static final int TIME_WAIT_BEFORE_JUMP = 1000; //ms
	public static final int TIME_JUMP_TO_NEXT_GROUND = 1000; //ms



	


	public int startLRMove;
	public int endLRMove;
	public int startRLMove;
	public int endRLMove;
	
	public CountdownParameters() {
		
		startLRMove = PADDING_GROUNDS_LEFT + GROUND_SIDESHIFT / 2;
		endLRMove = PADDING_GROUNDS_LEFT + GROUND_WIDTH - GROUND_SIDESHIFT / 2 ;
		startRLMove = endLRMove + GROUND_SIDESHIFT - SPRITE_WIDTH;
		endRLMove = startLRMove + GROUND_SIDESHIFT - SPRITE_WIDTH;
		
	}

	public int getY(int groundNo) {
		return PADDING_GROUNDS_TOP + (NUMBER_OF_GROUNDS - groundNo) * DISTANCE_BETWEEN_GROUNDS - PADDING_BETWEEN_GROUND_AND_SPRITE - SPRITE_HEIGHT;	
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
    
}
