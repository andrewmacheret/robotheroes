package com.macheret.robotheroes;

import java.awt.AWTException;

public class RobotHeroCLI {	
	public static void main(String ... args) {
		RobotHero robot = new RobotHero();
		try {
			robot.run();
		} catch (InterruptedException e) {
			System.out.println("Excecution interrupted - exiting");
		} catch (AWTException e) {
			// This exception can only really happen as a result
			// of trying to run this CLI in a very non-standard way 
			e.printStackTrace();
		}
	}
	
}
