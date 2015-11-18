package bot.action;

import bot.observers.unit.GenericUnitObserver;

public interface GenericAction extends GenericUnitObserver {

	public void checkAndActuate();
	public void onEndAction(boolean correct);
	public boolean isPossible();
}
