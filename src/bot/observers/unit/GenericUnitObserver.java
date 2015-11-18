package bot.observers.unit;

import bwapi.Unit;

public interface GenericUnitObserver {
	
	public void onUnit(Unit unit);
	public Unit getUnit();
	
	public void register();
	public void unRegister();
	
	
}
