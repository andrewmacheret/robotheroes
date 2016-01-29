package com.macheret.robotheroes;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import com.macheret.robotheroes.tasks.AscendTask;
import com.macheret.robotheroes.tasks.CheckEnabledTask;
import com.macheret.robotheroes.tasks.CooldownTask;
import com.macheret.robotheroes.tasks.EatCandyTask;
import com.macheret.robotheroes.tasks.KillEnemyTask;
import com.macheret.robotheroes.tasks.UpgradeHeroTask;

public class RobotHero {

	private static int KEY_LOCK_CODE = KeyEvent.VK_CAPS_LOCK;
	private static String KEY_LOCK_NAME = "CapsLock";
	private static long KEY_LOCK_CHECK_INTERVAL = 100;
	
	private static int GOLDEN_CLICK_SECONDS = 90;
	private static int ASCEND_MINUTES = 60;
	
	private static long CHECK_ENABLED_PERIOD = 100; // 10 times per second
	private static long KILL_ENEMY_PERIOD = 25; // 40 times per second (25 ms)
	private static long UPGRADE_HERO_PERIOD = TimeUnit.SECONDS.toMillis(5);
	private static long COOLDOWN_PERIOD = TimeUnit.SECONDS.toMillis(151);
	private static long EAT_CANDY_PERIOD = TimeUnit.SECONDS.toMillis(10);
	
	private static long CHECK_ENABLED_DELAY = TimeUnit.SECONDS.toMillis(0);
	private static long KILL_ENEMY_DELAY = TimeUnit.SECONDS.toMillis(1);
	private static long UPGRADE_HERO_DELAY = TimeUnit.SECONDS.toMillis(2);
	private static long COOLDOWN_DELAY = TimeUnit.SECONDS.toMillis(4);
	private static long EAT_CANDY_DELAY = TimeUnit.SECONDS.toMillis(0);
	
	private CheckEnabledTask checkEnabledTask;
	private KillEnemyTask killEnemyTask;
	private CooldownTask cooldownTask;
	private UpgradeHeroTask upgradeHeroTask;
	private EatCandyTask eatCandyTask;
	private AscendTask ascendTask;

	public void run() throws InterruptedException, AWTException {
		Robot robot = new Robot();
		
		// 1. turn caps off
		this.setKeyLock(false);
		
		// 2. record various mouse positions
		Point topLeft = this.logMouseOnKeyLock(true, "TOP LEFT CORNER");
		this.setKeyLock(false);
		
		// 3. make sure we're ready to go by confirming with 2 capslock toggles
		System.out.println("Confirm you're ready to go by turning capslock on");
		this.setKeyLock(true);
		
		// 4. now start clicking
		this.startClicking(robot, topLeft);
	}
	
	private void setKeyLock(boolean capslockState) throws InterruptedException {
		// short circuit if possible
		if (Toolkit.getDefaultToolkit().getLockingKeyState(KEY_LOCK_CODE) == capslockState) {
			System.out.println("Already set, " + KEY_LOCK_NAME + " enabled=" + capslockState);
			return;
		}
		
		// set the lock key
		//System.out.println("Setting " + KEY_LOCK_NAME + " to " + capslockState);
		//Toolkit.getDefaultToolkit().setLockingKeyState(KEY_LOCK_CODE, capslockState);
		
		// loop until the lock key is set to what we set it to
		System.out.println("Waiting until " + KEY_LOCK_NAME + " enabled=" + capslockState);
		do {
			if (Toolkit.getDefaultToolkit().getLockingKeyState(KEY_LOCK_CODE) == capslockState) {
				return;
			}
			
			Thread.sleep(KEY_LOCK_CHECK_INTERVAL);
		} while (true);
	}
	
	private Point logMouseOnKeyLock(boolean capslockState, String positionName) throws InterruptedException {
		System.out.println("Waiting for " + KEY_LOCK_NAME + " enabled=" + capslockState + " to record location of " + positionName);
		do {
			if (Toolkit.getDefaultToolkit().getLockingKeyState(KEY_LOCK_CODE) == capslockState) {
				return MouseInfo.getPointerInfo().getLocation();
			}
			
			Thread.sleep(KEY_LOCK_CHECK_INTERVAL);
		} while (true);
	}
	
	private Timer startClicking(Robot robot, Point topLeft) throws InterruptedException {
		System.out.println("Starting clickstooooorm!");
		
		Timer timer = new Timer("click-timer", false);
		
		checkEnabledTask = new CheckEnabledTask();
		
		killEnemyTask = new KillEnemyTask(
				robot,
				topLeft,
				checkEnabledTask
				);

		cooldownTask = new CooldownTask(
				robot,
				topLeft,
				checkEnabledTask,
				Arrays.asList(1),
				GOLDEN_CLICK_SECONDS
				);

		upgradeHeroTask = new UpgradeHeroTask(
				robot,
				topLeft,
				checkEnabledTask,
				cooldownTask
				);
		
		ascendTask = new AscendTask(robot, topLeft, checkEnabledTask, killEnemyTask, cooldownTask);
		
		eatCandyTask = new EatCandyTask(
				robot,
				topLeft,
				checkEnabledTask,
				ascendTask,
				ASCEND_MINUTES
				);
		
		timer.scheduleAtFixedRate(checkEnabledTask, CHECK_ENABLED_DELAY, CHECK_ENABLED_PERIOD);
		timer.scheduleAtFixedRate(killEnemyTask, KILL_ENEMY_DELAY, KILL_ENEMY_PERIOD);
		timer.scheduleAtFixedRate(cooldownTask, COOLDOWN_DELAY, COOLDOWN_PERIOD);
		timer.scheduleAtFixedRate(upgradeHeroTask, UPGRADE_HERO_DELAY, UPGRADE_HERO_PERIOD);
		timer.scheduleAtFixedRate(eatCandyTask, EAT_CANDY_DELAY, EAT_CANDY_PERIOD);
		
		return timer;
	}
}
