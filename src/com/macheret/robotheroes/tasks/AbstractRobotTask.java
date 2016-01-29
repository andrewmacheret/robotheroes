package com.macheret.robotheroes.tasks;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.util.TimerTask;

public abstract class AbstractRobotTask extends TimerTask {
	private Robot robot;
	private Point topLeft;
	
	public AbstractRobotTask(Robot robot, Point topLeft) {
		this.robot = robot;
		this.topLeft = topLeft;
	}
	
	protected void click(Point position, int... modifiers) {
		click(position.x, position.y, modifiers);
	}
	
	protected void click(int x, int y, int... modifiers) {
		robot.mouseMove(adjustX(x), adjustY(y));
		
		for (int i = 0; i < modifiers.length; i++) {
			robot.keyPress(modifiers[i]);
		}
		
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		
		for (int i = modifiers.length - 1; i >= 0; i--) {
			robot.keyRelease(modifiers[i]);
		}
	}
	
	protected void type(int keyCode, int... modifiers) {
		for (int i = 0; i < modifiers.length; i++) {
			robot.keyPress(modifiers[i]);
		}
		
		robot.keyPress(keyCode);
		robot.keyRelease(keyCode);
		
		for (int i = modifiers.length - 1; i >= 0; i--) {
			robot.keyRelease(modifiers[i]);
		}
	}
	
	protected Color getColor(Point position) {
		return getColor(position.x, position.y);
	}
	
	protected Color getColor(int x, int y) {
		return robot.getPixelColor(adjustX(x), adjustY(y));
	}
	
	protected Object getLockObject() {
		return robot;
	}
	
	protected int colorDifference(Color c1, Color c2) {
		return (int)Math.sqrt(Math.pow(c1.getRed() - c2.getRed(), 2)
				+ Math.pow(c1.getGreen() - c2.getGreen(), 2)
				+ Math.pow(c1.getBlue() - c2.getBlue(), 2));
	}
	
	protected BufferedImage capture(int x, int y, int width, int height) {
		return robot.createScreenCapture(new Rectangle(adjustX(x), adjustY(y), width, height));
	}
	
	protected void sleep(long ms) {
		robot.delay((int) ms);
	}
	
	protected void setAutoDelay(long ms) {
		robot.setAutoDelay((int) ms);
	}
	
	protected int getAutoDelay() {
		return robot.getAutoDelay();
	}
	
	private int adjustX(int x) {
		return topLeft.x + x;
	}
	
	private int adjustY(int y) {
		return topLeft.y + y;
	}
}
