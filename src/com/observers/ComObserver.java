package com.observers;

public interface ComObserver {

	public void onEndIteration(int i, int movimientos, int nume);
	public void onEndTrain();
	
	public void onActionTaken();
	public void onActionFail();
	
	public void onSendMessage(String s);
	public void onError(String s, boolean fatal);
	
	public void onFpsAverageAnnouncement(double fps);
	public void onDebugMessage(String s);
}
