package newAgent.agent;

import bot.action.GenericAction;

public interface OnEndActionObserver {
	public void onEndAction(GenericAction genericAction, boolean correct);
}
