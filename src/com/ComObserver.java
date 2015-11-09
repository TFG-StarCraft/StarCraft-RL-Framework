package com;

public interface ComObserver {

	public void onEndIteration();
	public void onEndTrain();
	
	public void onActionTaken();
	public void onActionFail();
	
	public void onSendMessage(String s);
	
}
