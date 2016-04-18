package com.observers;

import javax.swing.JPanel;

import bot.event.AbstractEvent;
import utils.DebugEnum;

public interface ComObserver {

	public void onEndIteration(int i, int movimientos, int nume, double alpha, double epsilon, Double r);
	public void onEndTrain();
	
	public void onActionTaken();
	public void onActionFail();
	
	public void onSendMessage(String s);
	public void onError(String s, boolean fatal);
	
	public void onFpsAverageAnnouncement(double fps);
	public void onDebugMessage(String s, DebugEnum level);
	public void onEvent(AbstractEvent abstractEvent);
	public void onFullQUpdate(JPanel panel);
}
