package bot.UnitWrapper;

import java.util.HashMap;

import bwapi.Unit;

public class BWAPI_UnitToWrapper {

	private final HashMap<Integer, UnitWrapper> map;

	public BWAPI_UnitToWrapper() {
		this.map = new HashMap<>();
	}

	public void put(UnitWrapper unit) {
		map.put(unit.getId(), unit);
	}

	public boolean contains(int id) {
		return map.containsKey(id);
	}

	public boolean contains(Unit unit) {
		return map.containsKey(unit.getID());
	}

	public boolean contains(UnitWrapper unit) {
		return map.containsValue(unit);
	}

	public UnitWrapper get(int id) {
		return map.get(id);
	}

	public UnitWrapper get(Unit unit) {
		return get(unit.getID());
	}

}
