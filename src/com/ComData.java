package com;

import bot.UnitWrapper.UnitWrapper;
import bot.action.movement.MoveAction;

public class ComData {

	public UnitWrapper unit;
	public boolean enBaliza;
	public int iniX;
	public int iniY;
	public boolean lastActionOk;

	public MoveAction action;
	
	public boolean restart;
	
	public ComData() {
		this.iniX = -1;
		this.iniY = -1;
	}
}
