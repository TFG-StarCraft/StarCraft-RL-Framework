package com.observers;

import newAgent.event.AbstractEvent;
import utils.DebugEnum;

public interface BotOberver {
	
	void onEvent(AbstractEvent code);
	void onSendMessage(String s);
	void onError(String s, boolean fatal);
	void onDebugMessage(String s, DebugEnum level);
	void onFpsAverageAnnouncement(double d);
	
}
