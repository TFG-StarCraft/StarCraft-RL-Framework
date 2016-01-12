package com.observers;

import utils.DebugEnum;

public interface BotOberver {

	public void onSendMessage(String s);
	public void onError(String s, boolean fatal);
	public void onDebugMessage(String s, DebugEnum level);
	public void onFpsAverageAnnouncement(double d);
}
