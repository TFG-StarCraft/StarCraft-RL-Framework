package com;

import java.util.List;

import bot.action.ActionDispatchQueue;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.WeaponType;

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
	
	public double getDistance() {
		List<Unit> l = getGroundUnitsInRange();
		if (!l.isEmpty()) {
			Unit u = l.get(0);
			//Calculate vector from unit to target.
			double vX = u.getX() - unit.getX();
			double vY = u.getY() - unit.getY();
			double modulo = Math.sqrt(vX*vX + vY*vY);
			return modulo;
		}
		//Otherwise, do nothing.
		else{
			return 0;
		}
	}
	
	private List<Unit> getGroundUnitsInRange() {
		
		Unit u = this.unit;
		UnitType t = u.getType();
		WeaponType w = t.groundWeapon();
		
		return this.unit.getUnitsInRadius(w.maxRange());
	}
}
