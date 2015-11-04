package bot.observers.unit;

import bot.action.GenericAction;
import bot.observers.GenericObserver;
import bwapi.Unit;

public class UniqueUnitObserver extends GenericObserver {

	private Unit unit;
	
	public UniqueUnitObserver(GenericAction action) {
		super(action);
	}

	@Override
	public void onUnit(Unit unit) {
		if (this.unit.getID() == unit.getID()) {
			action.checkAndActuate();
		}
	}
	
}
