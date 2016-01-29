package com.macheret.robotheroes.tasks;

import java.awt.Color;
import java.awt.Point;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

public class EatCandyTask extends AbstractRobotTask {
	// NOTE: These colors are based on data/Orangefish.png .
	// If the icon changes, these colors need to change.
	// Also these colors should be somehow loaded, not hard coded
	private Color[] COLORS_TO_FIND = new Color[] {
		new Color(0xffff5208),
		new Color(0xffff8205),
		new Color(0xffffde08),
		new Color(0xfff5e0b5),
	};
	
	private static int MINIMUM_COLOR_COUNT = 100;
	private static int COLOR_DIFFERENCE_THRESHOLD = 20;
	
	private static Point[] CANDY_LOCATIONS = new Point[] {
			new Point(514, 457),
			new Point(745, 345),
			new Point(852, 469),
			new Point(1042, 414),
			new Point(994, 423),
			new Point(741, 393),
	};
	
	private static int CANDY_WIDTH = 40;
	private static int CANDY_HEIGHT = 40;
	
	private CheckEnabledTask checkEnabledTask;
	private AscendTask ascendTask;
	private long millisToAscend;
	private long startTime;
	private int confirmed = 0;
	
	public EatCandyTask(Robot robot, Point topLeft, CheckEnabledTask checkEnabledTask, AscendTask ascendTask, long minutesToAscend) {
		super(robot, topLeft);
		this.checkEnabledTask = checkEnabledTask;
		this.ascendTask = ascendTask;
		this.millisToAscend = TimeUnit.MINUTES.toMillis(minutesToAscend);
		this.startTime = System.currentTimeMillis();
	}
	
	@Override
	public void run() {
		if (!checkEnabledTask.enabled()) return;
		synchronized(getLockObject()) {
			nextCandyLocation:
			for (Point candyLocation : CANDY_LOCATIONS) {
				BufferedImage capture = capture(candyLocation.x - CANDY_WIDTH/2, candyLocation.y - CANDY_HEIGHT/2, CANDY_WIDTH, CANDY_HEIGHT);
				
//				// debug:
//				ImageIcon icon = new ImageIcon();
//				icon.setImage(capture);
//				JOptionPane.showMessageDialog(null, icon);
//				int xxx = 0;
//				if (xxx == 0) xxx += 1;
				
				int colorCount = 0;
				for (Color colorToFind : COLORS_TO_FIND) {
					for (int x = 0; x < capture.getWidth(); x++) {
						for (int y = 0; y < capture.getHeight(); y++) {
							Color color = new Color(capture.getRGB(x, y) | 0xff000000);
							int diff = colorDifference(color, colorToFind);
							if (diff < COLOR_DIFFERENCE_THRESHOLD) {
								colorCount += 1;
							}
						}
					}
				}
				if (colorCount < MINIMUM_COLOR_COUNT) {
					continue nextCandyLocation;
				}
				
				// if we made it here, we found a candy
				System.out.println("Found candy at: " + candyLocation);
				
				// figure out if we should ascend
				long currentTime = System.currentTimeMillis();
				if (currentTime - startTime < millisToAscend) {
					// click
					System.out.println("CLICK!");
					click(candyLocation);
					//ascendTask.ascend(candyLocation);
				} else {
					confirmed += 1;
					if (confirmed < 3) {
						return;
					}
					confirmed = 0;
					
					// ascend
					System.out.println("ASCEND!");
					ascendTask.ascend(candyLocation);
					startTime = System.currentTimeMillis();
				}
				return;
			}
			
			confirmed = 0;
		}
	}
}
