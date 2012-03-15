package com.dreamboxx.client;

import java.util.Date;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.Line;
import org.vaadin.gwtgraphics.client.animation.Animate;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.Text;

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


import com.dreamboxx.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class KidsTime implements EntryPoint {

			private static final int TIME_PER_GROUND_IN_MILLISECONDS = 6000; // TODO: increase to 60'000 for full minutes
	//
	//		private static final int NUMBER_OF_LINES = 10;
	//
	//		private static final int LINE_LENGTH_DIFFERENCE = 80;
	//
	//		private static final int BORDER_PADDING_LR = 20;
	//
	//		private static final int COUNTDOWN_CANVAS_HEIGHT = 600;
	//
	//		private static final int COUNTDOWN_CANVAS_WIDTH = 800;
	//
	//		private static final int LINE_DISTANCE = 50;
	//
	//		private static final int LINE_HEIGHT = 5;
	//
//	private static final int ANIME_HEIGHT = 30;
//
//	private static final int ANIME_WIDTH = 30;




	public static final int COUNTDOWN_CANVAS_WIDTH = 1000;
	public static final int COUNTDOWN_CANVAS_HEIGHT = 500;
	public static final int NUMBER_OF_GROUNDS = 10;

	/*
	 * Grounds 
	 * 
	 * all sizes are given in pixels
	 * 
	 */
	public static final int GROUND_DEPTH = 5;
	public static final int DISTANCE_BETWEEN_GROUNDS = 32;
	public static final int PADDING_BETWEEN_GROUND_AND_SPRITE = 0;
	public static final int GROUND_WIDTH = 700;
	public static final int GROUND_SIDESHIFT = 100;
	public static final int PADDING_GROUNDS_TOP = 0;
	public static final int PADDING_GROUNDS_LEFT = 30;
	

	/* ANIMATION TIMES */ 

	/* 
	 * Total time for one ground: 1 minute
	 * Walking_time = 60'000 - (times below) 
	 *
	 * ALL times are given in Milliseconds
	 */

	public static final int TIME_WAIT_BEFORE_MOVE = 2000; // turn around during this wait
	public static final int TIME_WAIT_BEFORE_JUMP = 1000;
	public static final int TIME_JUMP = 2000;

	static final int SPRITE_HEIGHT = 30;
	static final int SPRITE_WIDTH = 30;

	
	int countdownTime = 10;
	
	/*



		HINT: only allow to increase times by 10 second steps, and make all WAIT/JUMP times below 10 seconds. This ensures that we do not have to position sprite „in the middle of a jump“

	 */

	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	Canvas canvas;
	Context2d context;
	final int canvasHeight = 300;
	final int canvasWidth = 300;


	DrawingArea countdownCanvas;
	public void onModuleLoad() {


		drawSpriteSelection();
		drawCountdownCanvas();
		
		drawTimeSelection(); // TODO

		




		//Prototypes:
		//		testHorizontalButtons();
		//		bouncingBall();
		//		displayClock();
		//		clickableCircles();
		//		drawRectangles();

	}

	public final static int NUMBER_OF_SPRITES = 1;

	private void drawSpriteSelection() {
		HorizontalPanel panel = new HorizontalPanel();
		//panel.setStyleName("demo-buttons");
		panel.setWidth("100%");
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);


		// need to use com.google.gwt... since vaadin has its own Image class used in our animation
		com.google.gwt.user.client.ui.Image[] sprites = new com.google.gwt.user.client.ui.Image[NUMBER_OF_SPRITES];
		PushButton buttons[] = new PushButton[NUMBER_OF_SPRITES];

		for (int i=0; i < NUMBER_OF_SPRITES; i++) {
			//TODO: select different images here for other sprites
			sprites[i] = new com.google.gwt.user.client.ui.Image("images/bunny/Bunny_hopps_frame_000" + (i+1) + ".gif");
			sprites[i].setPixelSize(30,30);
			buttons[i] = new PushButton(sprites[i]);
			buttons[i].setWidth("30px");
			buttons[i].setHeight("30px");
			buttons[i].setStylePrimaryName("spriteButton");
			panel.add(buttons[i]);

		}

		RootPanel.get("spritesContainer").add(panel);

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
		// TODO:
	}

	
	private void drawTimeSelection() {
		final RadioButton time1RadioButton = new RadioButton("Zeit", "1 Minuten");
		final RadioButton time2RadioButton = new RadioButton("Zeit", "5 Minuten");
		final RadioButton time3RadioButton = new RadioButton("Zeit", "10 Minuten");

		time1RadioButton.setValue(true);

		ClickHandler submitHandler = new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (time1RadioButton.getValue()) {
					setCountdownTime(1);
					startCountdown();
				}

				if (time2RadioButton.getValue()) {
					setCountdownTime(5);
					startCountdown();
				}

				if (time3RadioButton.getValue()) {
					setCountdownTime(10);
					startCountdown();
				}
}

		};

		Button submitButton = new Button("Countdown starten");
		submitButton.addClickHandler(submitHandler);

		VerticalPanel buttonPanel = new VerticalPanel();
		buttonPanel.add(time1RadioButton);
		buttonPanel.add(time2RadioButton);
		buttonPanel.add(time3RadioButton);
		buttonPanel.add(submitButton);
		RootPanel.get("controlsContainer").add(buttonPanel);

		
	}

	
	private void setCountdownTime(int timeInMinutes) {
		this.countdownTime = timeInMinutes;
	}

	private int getCountdownTime() {
		return countdownTime;
	}
	
	final CountdownParameters params = new CountdownParameters();
	
	public void startCountdown() {
		
		final int startGround = getCountdownTime() - 1; // TODO: at the moment countdownTime is only in full minutes; improve to consider also seconds
		int startX;
		int y;
		
		
		System.out.println("start countdown at " + getTime() +  " with " + getCountdownTime() + " minutes to go." );

		//Initial Position of Sprite for time mm:ss (minutes:seconds):    sprite is on ground mm    fraction to walk (if ss>JUMP TIME):        [ss – (WAIT_BEFORE_JUMP + JUMP)] / walkingTime * GROUND_WIDTH,        where walkingTime = 60-(WAIT_BEFORE_JUMP + JUMP + WAIT_BEFOREMOVE)
		// TODO: also use seconds to position sprite

		if (params.isLRmove(startGround)) { 
			startX =  params.startLRMove;
		}
		else {
			startX = params.startRLMove;
		}
		
		y = params.getY(startGround);
		System.out.println("initial y = " + y);
		
		final Image sprite = new Image(startX, y, 30,30, "images/bunny/Bunny_hopps.gif"); // TODO: distinguis left or right movement
		countdownCanvas.add(sprite);
		System.out.println("startground = " + startGround);
		
		// sprite.setY(sprite.getY() + DISTANCE_BETWEEN_GROUNDS);
		
		
				
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
				

				System.out.println("currentGround = " + currentGround + " startX = " + startX + ", endX = " + endX);
				
				if (currentGround > 0) {
					moveSpriteAlongLine(startX, endX);
					System.out.println("end of execute (TRUE) for ground " + currentGround);
					return true; //moves on to next ground
				} else {
					moveSpriteAlongLine(startX, endX);
					System.out.println("end of execute (FALSE) for ground " + currentGround);
					return false; //no more next grounds
				}
			}

			private void moveSpriteAlongLine(int fromX, int toX) {
	
				System.out.println("move alongLine: y = " + sprite.getY());
				
				if (fromX < toX) {
					System.out.println("move from left to right along line " + currentGround);
					System.out.println("fromX = " + fromX + " toX = " + toX);
				} else {
					System.out.println("move from right to left along line " + currentGround);
					System.out.println("fromX = " + fromX + " toX = " + toX);
				}

				Animate movement;
				movement = new Animate(sprite, "x", fromX, toX,  TIME_PER_GROUND_IN_MILLISECONDS); 
				movement.start();

				// go to next ground
				currentGround--;
				
			}
		};
		
		// execute command once explicitly because scheduled command only starts AFTER initial wait period
		myCommand.execute(); 
		if (startGround > 0) { 
			Scheduler.get().scheduleFixedPeriod(myCommand, TIME_PER_GROUND_IN_MILLISECONDS); 
		}
	}




	

	private String getTime() {
		Date date = new Date();
		DateTimeFormat dtf = DateTimeFormat.getFormat("dd.MM.yyyy (E) - HH:mm:ss:SS");
		return(dtf.format(date, TimeZone.createTimeZone(-60)));
	}







	// PROTOTYPES
	// can be removed!

//	public void startCountdown(final int timeInMinutes) {
//		//Window.alert("countdown mit " + timeInMinutes + " Minuten startet um " + getTime());
//
//
//		int initialLine = 5; // TODO: compute this from input
//		int x = BORDER_PADDING_LR;
//		int y = (NUMBER_OF_LINES-initialLine) * LINE_DISTANCE;
//		final Image animee = new Image(x, y, 30,30, "images/Green_choco.gif");
//		countdownCanvas.add(animee);
//
//		Scheduler.get().scheduleFixedPeriod(new RepeatingCommand() {
//			int currentLine = 5; // TODO: compute this from input
//
//			public boolean execute () {
//				int fromX;
//				int toX;
//				while (currentLine < NUMBER_OF_LINES) {
//					if (0 == currentLine % 2) {
//						// move from right to left
//						fromX = BORDER_PADDING_LR;
//						toX = COUNTDOWN_CANVAS_WIDTH-BORDER_PADDING_LR-LINE_LENGTH_DIFFERENCE;
//						moveAnimeAlongLine(fromX, toX);
//
//						return true;
//
//					} else {
//						// move from left to right
//						fromX = COUNTDOWN_CANVAS_WIDTH-BORDER_PADDING_LR-LINE_LENGTH_DIFFERENCE;
//						toX = BORDER_PADDING_LR;
//						moveAnimeAlongLine(fromX, toX);
//						return true;
//					}
//
//
//
//				}
//				return false;
//			}
//			private void moveAnimeAlongLine(int fromX, int toX) {
//				if (fromX < toX) {
//					System.out.println("move from left to right along line " + currentLine);
//				} else {
//					System.out.println("move from right to left along line " + currentLine);
//				}
//
//				Animate movement;
//				movement = new Animate(animee, "x", fromX, toX,  TIME_PER_LINE_IN_MILLISECONDS*timeInMinutes); // TODO: increase to 60'000 for full minutes
//				movement.start();
//
//				// go to next line
//				currentLine++;
//				animee.setY(currentLine * LINE_DISTANCE);
//			}
//
//		}
//		, TIME_PER_LINE_IN_MILLISECONDS);
//	}





	//		private void testHorizontalButtons() {
	//			
	//			final HorizontalPanel panel = new HorizontalPanel();
	//			//panel.setStyleName("demo-panel");
	//
	//			Button add = new Button("Add");
	//			Button clear = new Button("Clear");
	//			Button third = new Button ("third");
	//			PushButton pushButton = new PushButton(new com.google.gwt.user.client.ui.Image("images/bunny/Bunny_hopps_frame_0001.gif"));
	//			
	//			HorizontalPanel buttons = new HorizontalPanel();
	//			//buttons.setStyleName("demo-buttons");
	//			buttons.setWidth("100%");
	//			buttons.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	//			
	//			buttons.add(add);
	//			add.setWidth("50px");
	//
	//			buttons.add(clear);
	//			clear.setWidth("50px");
	//			
	//			buttons.add(pushButton);
	//			pushButton.setWidth("50px");
	//			pushButton.setHeight("50px");
	//			
	//			buttons.add(third);
	//			third.setWidth("50px");
	//			
	//			
	//			RootPanel.get("animation").add(buttons);
	//			RootPanel.get("sprites_container").add(panel);
	//		}
	//					
	//		private void testHorizontalButtons2() {
	//				
	//			
	//			final HorizontalPanel panel = new HorizontalPanel();
	//			//panel.setStyleName("demo-panel");
	//			
	//			Image[] sprites = new Image[3];
	//			sprites[0] = new Image(20,20,50,50,"images/bunny/Bunny_hopps_frame_0001.gif");
	//			sprites[1] = new Image(0,0,50,50,"images/bunny/Bunny_hopps_frame_0002.gif");
	//			sprites[2] = new Image(0,0,50,50,"images/bunny/Bunny_hopps_frame_0003.gif");
	//			
	////			spriteArea.add(sprites[0]);
	////			spriteArea.add(sprites[1]);
	////			spriteArea.add(sprites[2]);
	//			//RootPanel.get("sprites_container").add(spriteArea);
	//			
	////			DrawingArea[] spriteAreas = new DrawingArea[3];
	////			spriteAreas[0] = new DrawingArea(80,80);
	////			spriteAreas[1] = new DrawingArea(80,80);
	////			spriteAreas[2] = new DrawingArea(80,80);
	////			spriteAreas[0].add(sprites[0]);
	////			spriteAreas[1].add(sprites[1]);
	////			spriteAreas[2].add(sprites[2]);
	////			RootPanel.get("sprite0").add(spriteAreas[0]);
	////			RootPanel.get("sprite1").add(spriteAreas[1]);
	////			RootPanel.get("sprite2").add(spriteAreas[2]);
	//		
	//
	//			HorizontalPanel buttons = new HorizontalPanel();
	//			//buttons.setStyleName("demo-buttons");
	//
	//			buttons.add(sprites[0]);
	//			buttons.add(sprites[1]);
	//			buttons.add(sprites[2]);
	//
	//			RootPanel.get("sprites_container").add(buttons);
	//			//RootPanel.get("demo").add(panel);
	//
	//			
	//		}
	//		
	//		private void clickableCircles() {
	//			final DrawingArea vcanvas = new DrawingArea(400, 400);
	//			RootPanel.get().add(vcanvas);
	//
	//			ClickHandler handler = new ClickHandler() {
	//				public void onClick(ClickEvent event) {
	//					vcanvas.bringToFront((Circle) event.getSource());
	//				}
	//			};
	//			int xCoords[] = { 200, 225, 175 };
	//			int yCoords[] = { 150, 200, 200 };
	//			String fillColors[] = { "red", "blue", "green" };
	//			for (int i = 0; i < xCoords.length; i++) {
	//				Circle circle = new Circle(xCoords[i], yCoords[i], 50);
	//				circle.setFillColor(fillColors[i]);
	//				circle.setFillOpacity(0.5);
	//				circle.addClickHandler(handler);
	//				vcanvas.add(circle);
	//			}
	//		}
	//
	//		private void drawRectangles() {
	//			canvas = Canvas.createIfSupported();
	//
	//			//		if (canvas == null) {
	//			//			RootPanel.get().add(new Label("Sorry, your browser doesn't support the HTML5 Canvas element"));
	//			//			return;
	//			//		}
	//
	//			canvas.setStyleName("mainCanvas");
	//			canvas.setWidth(canvasWidth + "px");
	//			canvas.setCoordinateSpaceWidth(canvasWidth);
	//
	//			canvas.setHeight(canvasHeight + "px");
	//			canvas.setCoordinateSpaceHeight(canvasHeight);
	//
	//			RootPanel.get("canvasContainer").add(canvas);
	//			context = canvas.getContext2d();
	//
	//			final Timer timer = new Timer() {
	//				@Override
	//				public void run() {
	//					drawSomethingNew();
	//				}
	//			};
	//			timer.scheduleRepeating(1500);
	//		}
	//
	//		private void displayClock() {
	//			final Button sendButton = new Button("Start/Stop Timer");
	//			final TextBox nameField = new TextBox();
	//			nameField.setText("todo");
	//			final Label errorLabel = new Label();
	//
	//			// We can add style names to widgets
	//			sendButton.addStyleName("sendButton");
	//
	//			// Add the nameField and sendButton to the RootPanel
	//			// Use RootPanel.get() to get the entire body element
	//			RootPanel.get("nameFieldContainer").add(nameField);
	//			RootPanel.get("sendButtonContainer").add(sendButton);
	//			RootPanel.get("errorLabelContainer").add(errorLabel);
	//
	//
	//			final Label timerLabel = new Label();
	//			timerLabel.setText("timer sample");
	//			RootPanel.get("timerContainer").add(timerLabel);
	//
	//			//		Date date = new Date();
	//			//		DateTimeFormat dtf = DateTimeFormat.getFormat("dd.MM.yyyy (E) - HH:mm:ss:SS");
	//			//		Window.alert(dtf.format(date, TimeZone.createTimeZone(-1)));
	//
	//
	//
	//
	//			final Timer t = new Timer() {
	//				int i = 0;
	//				public void run() {
	//					timerLabel.setText("i = " + i++ + " Time: " + getTime());
	//
	//				}
	//
	//
	//			};
	//
	//
	//			// Schedule the timer to run once in 5 seconds.
	//			t.scheduleRepeating(1000);
	//			class MyHandler implements ClickHandler {
	//				/**
	//				 * Fired when the user clicks on the sendButton.
	//				 */
	//				boolean isRunning = true;
	//				public void onClick(ClickEvent event) {
	//					if (isRunning) {
	//						t.cancel();
	//						isRunning = false;
	//					}
	//					else {
	//						t.scheduleRepeating(1000);
	//						isRunning = true;
	//
	//					}
	//				}
	//			}
	//			MyHandler handler2 = new MyHandler();
	//			sendButton.addClickHandler(handler2);
	//		}
	//
	//
	//		private void bouncingBall() {
	//			DrawingArea bounceCanvas = new DrawingArea(400, 400);
	//			RootPanel.get().add(bounceCanvas);
	//
	//
	//
	//			int x = 100;
	//			int y = 200;
	//			int radius = 50;
	//			Circle ball = new Circle(x, y, radius);
	//			ball.setFillColor("red");
	//			ball.setFillOpacity(0.5);
	//			bounceCanvas.add(ball);
	//
	//			final Animate makeSmallAnimation = new Animate(ball, "radius", 300, 0, 3000);
	//
	//			final Animate enlargeAnimation = new Animate(ball, "radius", 0, 300, 3000) {
	//				@Override
	//				public void onComplete() {
	//					makeSmallAnimation.start();
	//				}
	//			};
	//
	//
	//			Scheduler.get().scheduleFixedPeriod(new RepeatingCommand() {
	//				Integer counter = 0;
	//				public boolean execute () {
	//					enlargeAnimation.start();
	//					// run 5 times
	//					counter = counter + 1;
	//					return (counter < 5);
	//				}
	//			}
	//			, 6000);
	//		}
	//
	//		public void drawSomethingNew() {
	//
	//			// Get random coordinates and sizing
	//			int rndX = Random.nextInt(canvasWidth);
	//			int rndY = Random.nextInt(canvasHeight);
	//			int rndWidth = Random.nextInt(canvasWidth);
	//			int rndHeight = Random.nextInt(canvasHeight);
	//
	//			// Get a random color and alpha transparency
	//			int rndRedColor = Random.nextInt(255);
	//			int rndGreenColor = Random.nextInt(255);
	//			int rndBlueColor = Random.nextInt(255);
	//			double rndAlpha = Random.nextDouble();
	//
	//			CssColor randomColor = CssColor.make("rgba(" + rndRedColor + ", " + rndGreenColor + "," + rndBlueColor + ", " + rndAlpha + ")");
	//
	//			context.setFillStyle(randomColor);
	//			context.fillRect( rndX, rndY, rndWidth, rndHeight);
	//			context.fill();
	//
	//
	//
	//
	//
	//		}
//	
//	
//	private void addTimeSelection() {
//		final RadioButton time1RadioButton = new RadioButton("Zeit", "1 Minuten");
//		final RadioButton time2RadioButton = new RadioButton("Zeit", "2 Minute");
//
//		time1RadioButton.setValue(true);
//
//		ClickHandler submitHandler = new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				if (time1RadioButton.getValue()) {
//					startCountdown(1);
//				}
//
//				if (time2RadioButton.getValue()) {
//					startCountdown(2);
//				}
//			}
//
//		};
//
//		Button submitButton = new Button("Countdown starten");
//		submitButton.addClickHandler(submitHandler);
//
//		VerticalPanel buttonPanel = new VerticalPanel();
//		buttonPanel.add(time1RadioButton);
//		buttonPanel.add(time2RadioButton);
//		buttonPanel.add(submitButton);
//		RootPanel.get().add(buttonPanel);
//
//
//	}
//
//
//

}	


