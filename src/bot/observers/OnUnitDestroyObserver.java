package bot.observers;

import bot.observers.unit.GenericUnitObserver;
import bwapi.Unit;

public interface OnUnitDestroyObserver extends GenericUnitObserver {

	public void onUnitDestroy(Unit unit);
		
}
