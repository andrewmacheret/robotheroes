package com.macheret.robotheroes.tasks;

import java.awt.Color;
import java.awt.Point;
import java.awt.Robot;

public class UpgradeHeroTask extends AbstractRobotTask {
	//private static Color GILD_COLOR = new Color(0xffffdb39);
	private static int GILD_COLOR_THRESHOLD = 20;
	private static Color NO_GILD_COLOR = new Color(0xfff3e997);
	
	private static Point LEVEL_CURRENT_HERO = new Point(94, 363);
	private static Point CHECK_GILDED = new Point(163, 372);
	private static Point SCROLL_DOWN = new Point(547, 621);
	private static Point BUY_AVAILABLE_UPGRADES = new Point(364, 551);
	
	private CheckEnabledTask checkEnabledTask;
	private CooldownTask cooldownTask;
	
	public UpgradeHeroTask(Robot robot, Point topLeft, CheckEnabledTask checkEnabledTask, CooldownTask cooldownTask) {
		super(robot, topLeft);
		this.checkEnabledTask = checkEnabledTask;
		this.cooldownTask = cooldownTask;
	}
	
	@Override
	public void run() {
		if (!checkEnabledTask.enabled()) return;
		synchronized(getLockObject()) {
			if (!cooldownTask.allowLeveling()) return;
			
			// check if gilded
			Color color = getColor(CHECK_GILDED);
			boolean gilded = colorDifference(color, NO_GILD_COLOR) > GILD_COLOR_THRESHOLD;
			
			// if not gilded, scroll down
			if (!gilded) {
				// click it 3 times
				click(SCROLL_DOWN);
				click(SCROLL_DOWN);
				click(SCROLL_DOWN);
			}
			
			// always upgrade hero
			click(BUY_AVAILABLE_UPGRADES);
			
			// level up (with command key)
			// TODO: command key doesn't seem to work correctly... clicking 20 times instead
			for (int i=0; i<20; i++) {
				click(LEVEL_CURRENT_HERO);
			}
		}
	}
}
