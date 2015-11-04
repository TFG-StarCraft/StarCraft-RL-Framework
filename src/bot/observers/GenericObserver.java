package bot.observers;

import bot.action.GenericAction;
import bwapi.Unit;

public abstract class GenericObserver {

	protected GenericAction action;
	
	public GenericObserver(GenericAction action) {
		this.action = action;
	}
	
	public abstract void onUnit(Unit unit);
	
}
