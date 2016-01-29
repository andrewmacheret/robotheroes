package com.macheret.robotheroes.tasks;

import java.awt.Point;
import java.awt.Robot;
import java.util.concurrent.TimeUnit;

public class AscendTask extends AbstractRobotTask {
	
	public static class HeroInfo {
		private String name;
		private Point location;
		private int allUpgradesLevel;
		
		public HeroInfo(String name, Point location, int allUpgradesLevel) {
			this.name = name;
			this.location = location;
			this.allUpgradesLevel = allUpgradesLevel;
		}

		public Point getLocation() {
			return location;
		}

		public int getAllUpgradesLevel() {
			return allUpgradesLevel;
		}
		
		public String getName() {
			return name;
		}
	}
	
	private static HeroInfo[][] INITIAL_HEROES = new HeroInfo[][] {
		{
		  new HeroInfo("Cid",  new Point(94, 229), 150),
		  new HeroInfo("Tree", new Point(94, 338), 100),
		  new HeroInfo("Ivan", new Point(94, 444), 125),
		  new HeroInfo("Brit", new Point(94, 550), 75),
		},{
		  new HeroInfo("Fish", new Point(94, 249), 100),
		  new HeroInfo("Bett", new Point(94, 358), 100),
		  new HeroInfo("Sam",  new Point(94, 464), 75),
		  new HeroInfo("Leon", new Point(94, 570), 75),
		},{
		  new HeroInfo("Seer", new Point(94, 269), 75),
		  new HeroInfo("Alex", new Point(94, 378), 100),
		  new HeroInfo("Nata", new Point(94, 484), 75),
		  new HeroInfo("Merc", new Point(94, 590), 100),
		},{
		  new HeroInfo("Bobb", new Point(94, 239), 100),
		  new HeroInfo("Broy", new Point(94, 348), 100),
		  new HeroInfo("Geor", new Point(94, 454), 100),
		  new HeroInfo("Mida", new Point(94, 560), 125),
		},{
		  new HeroInfo("Ref",  new Point(94, 209), 125),
		  new HeroInfo("Abad", new Point(94, 318), 75),
		  new HeroInfo("Ma",   new Point(94, 424), 75),
		  new HeroInfo("Amen", new Point(94, 530), 150),
		},{
		  new HeroInfo("Beas", new Point(94, 229), 100),
		  new HeroInfo("Athe", new Point(94, 338), 100),
		  new HeroInfo("Aphr", new Point(94, 444), 125),
		  new HeroInfo("Shin", new Point(94, 550), 100),
		},{
		  new HeroInfo("Gran", new Point(94, 255), 75),
		  new HeroInfo("Fros", new Point(94, 363), 75),
		}
	};
	private static int[] INITIAL_HEROES_SCROLL = {7, 7, 8, 8, 7, 8, 0};
	
	private static Point HERO_SCREEN = new Point(40, 100);
	private static Point RELIC_SCREEN = new Point(321, 100);
	private static Point PROGRESSION_TOGGLE = new Point(1112, 248);
	private static Point SCROLL_DOWN = new Point(547, 621);
	private static Point ASCEND = new Point(1111, 288);
	private static Point ASCEND_CONFIRM = new Point(490, 481);
	
	// TODO
	private static Point SALVAGE = new Point(276, 441);
	private static Point SALVAGE_CONFIRM = new Point(488, 399);
	
	private CheckEnabledTask checkEnabledTask;
	private KillEnemyTask killEnemyTask;
	private CooldownTask cooldownTask;
	private Point candy;
	
	public AscendTask(Robot robot, Point topLeft, CheckEnabledTask checkEnabledTask, KillEnemyTask killEnemyTask, CooldownTask cooldownTask) {
		super(robot, topLeft);
		this.checkEnabledTask = checkEnabledTask;
		this.killEnemyTask = killEnemyTask;
		this.cooldownTask = cooldownTask;
	}
	
	public void ascend(Point candy) {
		this.candy = candy;
		
		this.run();
	}
	
	@Override
	public void run() {
		if (!checkEnabledTask.enabled()) return;
		if (candy == null) return;
		synchronized(getLockObject()) {
			
			// 1. stop all actions
			checkEnabledTask.stop();
			
			// we're about to do a LOT of clicking, so let's temporarily set an autodelay
			int autoDelay = getAutoDelay();
			setAutoDelay(2);
			
			// 2. salvage
			click(RELIC_SCREEN);
			click(SALVAGE);
			click(SALVAGE_CONFIRM);
			
			// 3. click ascend (yes on confirmation)
			click(ASCEND);
			click(ASCEND_CONFIRM);
			
			// 4. toggle progression
			sleep(TimeUnit.SECONDS.toMillis(1));
			click(PROGRESSION_TOGGLE);
			
			// 5. click candy and collect money
			click(candy);
			sleep(TimeUnit.SECONDS.toMillis(3));
			killEnemyTask.collectAllGold();
			
			// 6. level all heroes
			click(HERO_SCREEN);
			for (int i=0; i<INITIAL_HEROES.length; i++) {
				for (HeroInfo hero : INITIAL_HEROES[i]) {
					for (int j = 0; j < hero.getAllUpgradesLevel(); j++) {
						click(hero.getLocation());
					}
				}
				for (int j=0; j<INITIAL_HEROES_SCROLL[i]; j++) {
					click(SCROLL_DOWN);
					sleep(100);
				}
				sleep(100);
			}
			
			// 7. reset cooldowns
			cooldownTask.reset();
			
			setAutoDelay(autoDelay);
			
			// 8. start all actions
			checkEnabledTask.start();
		}
	}
}
