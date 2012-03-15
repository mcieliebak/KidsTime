package com.dreamboxx.client;


public class CountdownParameters {
    
	public int startLRMove;
	public int endLRMove;
	public int startRLMove;
	public int endRLMove;
	
	public CountdownParameters() {
		
		startLRMove = KidsTime.PADDING_GROUNDS_LEFT + KidsTime.GROUND_SIDESHIFT / 2;
		endLRMove = KidsTime.PADDING_GROUNDS_LEFT + KidsTime.GROUND_WIDTH + KidsTime.GROUND_SIDESHIFT / 2 + - KidsTime.SPRITE_WIDTH;
		startRLMove = endLRMove;
		endRLMove = startLRMove;
		
	}

	public int getY(int groundNo) {
		return KidsTime.PADDING_GROUNDS_TOP + (KidsTime.NUMBER_OF_GROUNDS - groundNo) * KidsTime.DISTANCE_BETWEEN_GROUNDS - KidsTime.PADDING_BETWEEN_GROUND_AND_SPRITE - KidsTime.SPRITE_HEIGHT;	
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
