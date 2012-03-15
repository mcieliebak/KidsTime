import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//TODO: add packages for slf4j and logback to classpath


public class CountdownAnimation implements CountdownListener {


	Logger logger = LoggerFactory.getLogger("CountdownAnimation");
	

	private DrawingArea countdownCanvas;
	private final CountdownParameters params = new CountdownParameters();
	private CountdownController countdownController;







	public CountdownAnimation (CountdownController countdownController) {
		countdownController.addCountdownListener(this);
		
		drawCountdownCanvas();

		
	}

	private void drawCountdownCanvas() {

		countdownCanvas = new DrawingArea(COUNTDOWN_CANVAS_WIDTH, COUNTDOWN_CANVAS_HEIGHT);
		countdownCanvas.setStylePrimaryName("animationContainer");
		RootPanel.get("animationContainer").add(countdownCanvas);

		drawGrounds();
		drawGoal();

	}


	private void drawGrounds(){

		Rectangle ground;
		int xLeft, width, yTop, depth;

		for (int i = 0; i < NUMBER_OF_GROUNDS; i++) {
			if (0 == i % 2) { 
				// i is even: ground is shifted to the left
				xLeft =  PADDING_GROUNDS_LEFT;
				width = GROUND_WIDTH;
				yTop = PADDING_GROUNDS_TOP + (NUMBER_OF_GROUNDS - i) * DISTANCE_BETWEEN_GROUNDS;
				depth = GROUND_DEPTH;

			}
			else {
				// i is odd: ground is shifted to the right
				xLeft = PADDING_GROUNDS_LEFT + GROUND_SIDESHIFT;       
				width = GROUND_WIDTH;
				yTop = PADDING_GROUNDS_TOP + (NUMBER_OF_GROUNDS - i) * DISTANCE_BETWEEN_GROUNDS;    
				depth = GROUND_DEPTH;

			}

			ground = new Rectangle(xLeft, yTop, width, depth); 

			ground.setStrokeWidth(2);
			ground.setStrokeColor("blue");
			ground.setFillColor("darkblue");
			ground.setRoundedCorners(2);

			countdownCanvas.add(ground);
		}
	}

	private void drawGoal() {
		// TODO: do this
	}


	@Override
	public void startCountdown() {
		logger.debug("countdown start");
		
		// grounds are numbered starting with 0 for lowest ground
		// each ground covers exactly one minute
		// hence, starting ground is countdown-time - 1
		final int startGround = countdownController.getStartTime() - 1; // TODO: at the moment countdownTime is only in full minutes; improve to consider also seconds
		int startX;
		int y;


		//System.out.println("start countdown at " + getTime() 
		    +  " with " + getCountdownTime() + " minutes to go." );

		if (params.isLRmove(startGround)) { 
			startX =  params.startLRMove;
		}
		else {
			startX = params.startRLMove;
		}

		y = params.getY(startGround);
		System.out.println("initial y = " + y);

		// TODO: use sprite selected by SpriteSelection
		final Image sprite = new Image(startX, y, 30,30, "images/bunny/Bunny_hopps.gif"); // TODO: distinguis left or right movement
		countdownCanvas.add(sprite);
		
		RepeatingCommand myCommand = new RepeatingCommand() {

			int startX;
			int endX;
			int currentGround = startGround;
			int y;

			public boolean execute () {

				// position sprite on starting position on currentGround
				if (params.isLRmove(currentGround)) {
					startX =  params.startLRMove;
					endX = params.endLRMove;
				}
				else {
					startX = params.startRLMove;
					endX = params.endRLMove;
				}

				y = params.getY(currentGround);
				sprite.setY(y);



				if (currentGround > 0) {
					moveSpriteAlongLine(startX, endX);
					return true; //moves on to next ground
				} else {
					moveSpriteAlongLine(startX, endX);
					return false; //no more next grounds
				}
			}

			private void moveSpriteAlongLine(int fromX, int toX) {

				logger.debug(": y = " + sprite.getY());

				if (fromX < toX) {
					logger.debug("moveSpriteAlongLine: move from left to right along line " + currentGround + ", fromX = " + fromX + ", toX = " + toX);
				} else {
					logger.debug("moveSpriteAlongLine: move from right to left along line " + currentGround + ", fromX = " + fromX + ", toX = " + toX);
				}

				Animate movement;
				movement = new Animate(sprite, "x", fromX, toX,  CountdownParameters.TIME_PER_GROUND); 
				movement.start();

				// go to next ground
				currentGround--;

			}
		};

		// execute command once explicitly because scheduled command only starts AFTER initial wait period
		myCommand.execute(); 
		if (startGround > 0) { 
			Scheduler.get().scheduleFixedPeriod(myCommand, CountdownParameters.TIME_PER_GROUND); 
		}



		private String getTime() {
			Date date = new Date();
			DateTimeFormat dtf = DateTimeFormat.getFormat("dd.MM.yyyy (E) - HH:mm:ss:SS");
			return(dtf.format(date, TimeZone.createTimeZone(-60)));
		}


		@Override
		public void updateStartTime(int startTime) {
			//TODO: position Sprite

		}

	
		@Override
		public void finishCountdown() {
			//do nothing
		}

	}
