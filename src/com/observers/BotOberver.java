package com.observers;

public interface BotOberver {

	public void onSendMessage(String s);
	public void onError(String s, boolean fatal);
	public void onDebugMessage(String s);
	public void onFpsAverageAnnouncement(double d);
}
