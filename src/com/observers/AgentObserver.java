package com.observers;

public interface AgentObserver {

	public void onEndIteration(int movimientos, int nume, int i);
	public void onEndTrain();
	
	public void onActionTaken();
	public void onActionFail();
	
	public void onSendMessage(String s);
	
}
