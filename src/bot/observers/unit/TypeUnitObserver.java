package bot.observers.unit;

import bot.action.GenericAction;
import bot.observers.GenericObserver;
import bwapi.Unit;
import bwapi.UnitType;

public class TypeUnitObserver extends GenericObserver {

	protected UnitType type;
	
	public TypeUnitObserver(GenericAction action) {
		super(action);
	}

	@Override
	public void onUnit(Unit unit) {
		if (type.equals(unit.getType())) {
			action.checkAndActuate();
		}
	}

}
