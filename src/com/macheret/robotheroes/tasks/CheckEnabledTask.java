package com.macheret.robotheroes.tasks;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.TimerTask;

public class CheckEnabledTask extends TimerTask {
	private boolean capslockOn = false;
	private boolean manuallyDisabled = false;
	
	public CheckEnabledTask() {
		//
	}
	
	@Override
	public void run() {
		capslockOn = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
	}
	
	public boolean enabled() {
		return capslockOn && !manuallyDisabled;
	}
	
	public void stop() {
		manuallyDisabled = true;
	}
	
	public void start() {
		manuallyDisabled = false;
	}
}
