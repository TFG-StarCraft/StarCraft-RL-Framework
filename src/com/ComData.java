package com;

import bot.action.ActionDispatchQueue;
import bwapi.Unit;

public class ComData {

	public Unit unit;
	public boolean onFinal;
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
}
