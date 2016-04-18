package com;

import bot.action.ActionDispatchQueue;
import bwapi.Unit;

public class ComData {

	public Unit unit;
	public int iniX;
	public int iniY;
	public boolean lastActionOk;

	public ActionDispatchQueue actionQueue;

	public boolean restart;

	public ComData(Com com) {
		this.actionQueue = new ActionDispatchQueue(com);

		this.iniX = -1;
		this.iniY = -1;
	}

	private boolean onFinal;
//	private boolean isUpdated;
//	private Semaphore s = new Semaphore(0);
//	private Semaphore mutex = new Semaphore(1);
	public boolean isFinalStateGoal;

	// TODO
	public void setOnFinal(boolean onFinal) {
		this.onFinal = onFinal;
	}

	public boolean getOnFinalUpdated() {
		return onFinal;
	}

	public void resetFinal() {
		this.onFinal = false;
	}
}
