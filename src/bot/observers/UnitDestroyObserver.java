package bot.observers;

import bot.observers.unit.GenericUnitObserver;
import bwapi.Unit;

public interface UnitDestroyObserver extends GenericUnitObserver {

	public void onUnitDestroy(Unit unit);
	public void unRegisterUnitDestroy();
		
}
