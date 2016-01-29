package com.macheret.robotheroes.tasks;

import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CooldownTask extends AbstractRobotTask {
	private static int[][] COOLDOWNS_LIST = {
			{1,2,3,4,5,7,8,6,9},
			{},
			{},
			{},
			{},
			{},
			{8,9,1,2,3,4,5,7},
			{1,2},
			{1,2},
			{1,2,3,4},
			{1,2},
			{1,2},
	};
	
	// slight overkill... KeyEvent.VK_1 is the same value as '1', etc.
	private static Map<Integer, Integer> codes = new HashMap<>();
	static {
		codes.put(1, KeyEvent.VK_1);
		codes.put(2, KeyEvent.VK_2);
		codes.put(3, KeyEvent.VK_3);
		codes.put(4, KeyEvent.VK_4);
		codes.put(5, KeyEvent.VK_5);
		codes.put(6, KeyEvent.VK_6);
		codes.put(7, KeyEvent.VK_7);
		codes.put(8, KeyEvent.VK_8);
		codes.put(9, KeyEvent.VK_9);
	}
	
	private CheckEnabledTask checkEnabledTask;
	private long goldenClicksDurationMillis;
	
	private long lastCooldownTime = 0;
	private Set<Integer> lastCooldowns = new HashSet<>();
	
	private Set<Integer> ignore;
	
	private int cooldownIndex = 0;
	
	public CooldownTask(Robot robot, Point topLeft, CheckEnabledTask checkEnabledTask, Collection<Integer> ignore, long goldenClicksDurationSeconds) {
		super(robot, topLeft);
		this.checkEnabledTask = checkEnabledTask;
		this.ignore = new HashSet<>(ignore);
		this.goldenClicksDurationMillis = TimeUnit.SECONDS.toMillis(goldenClicksDurationSeconds);
	}
	
	@Override
	public void run() {
		synchronized(getLockObject()) {
			int[] cooldowns = COOLDOWNS_LIST[cooldownIndex];
			cooldownIndex = (cooldownIndex + 1) % COOLDOWNS_LIST.length;
			
			// if not enabled, still cycle the cooldowns
			if (!checkEnabledTask.enabled()) {
				System.out.println("[" + new Date() + "] Missed cooldowns: " + Arrays.toString(cooldowns));
				return;
			}
			
			System.out.println("[" + new Date() + "] Cooldowns: " + Arrays.toString(cooldowns));
			
			int autoDelay = getAutoDelay();
			setAutoDelay(1);
			
			for (int cooldown : cooldowns) {
				if (!ignore.contains(cooldown)) {
					type(codes.get(cooldown));
				}
			}
			
			setAutoDelay(autoDelay);
			
			lastCooldownTime = System.currentTimeMillis();
			
			lastCooldowns.clear();
			for (int cooldown : cooldowns) {
				lastCooldowns.add(cooldown);
			}
		}
	}
	
	public boolean allowLeveling() {
		long time = System.currentTimeMillis();
		
		// deny leveling if the last cooldown contained golden clicks AND we're within ~90 seconds (or whatever of the last golden click)
		return (time - lastCooldownTime > goldenClicksDurationMillis) || !lastCooldowns.contains(5);
	}
	
	public void reset() {
		cooldownIndex = 0;
		lastCooldownTime = 0;
		lastCooldowns.clear();
	}
}
