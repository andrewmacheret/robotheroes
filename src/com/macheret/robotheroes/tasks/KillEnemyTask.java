package com.macheret.robotheroes.tasks;

import java.awt.Point;
import java.awt.Robot;
import java.util.Random;

public class KillEnemyTask extends AbstractRobotTask {
	private static Point ENEMY_GOLD = new Point(857, 432);
	private static int X_VARIANCE = 100;
	
	private CheckEnabledTask checkEnabledTask;
	private Random random = new Random();
	
	public KillEnemyTask(Robot robot, Point topLeft, CheckEnabledTask checkEnabledTask) {
		super(robot, topLeft);
		this.checkEnabledTask = checkEnabledTask;
	}
	
	@Override
	public void run() {
		if (!checkEnabledTask.enabled()) return;
		synchronized(getLockObject()) {
			int dx = X_VARIANCE > 0 ? (int)(random.nextDouble() * X_VARIANCE * 2) - X_VARIANCE : 0;
			clickWithOffset(dx);
		}
	}
	
	public void collectAllGold() {
		for (int dx = -X_VARIANCE; dx <= X_VARIANCE; dx += 10) {
			clickWithOffset(dx);
		}
	}
	
	private void clickWithOffset(int dx) {
		click(ENEMY_GOLD.x + dx, ENEMY_GOLD.y);
	}
}
