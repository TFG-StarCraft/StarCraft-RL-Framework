package bot.commonFunctions;

import java.util.ArrayList;
import java.util.List;

import bwapi.Unit;
import bwapi.UnitType;
import bwapi.WeaponType;

/**
 * Set of functions which help to determine the features of the units in the
 * environment.
 * 
 * @author Alberto Casas Ortiz
 * @author Raúl Martín Guadaño
 * @author Miguel Ascanio Gómez
 */
public class CheckAround {

	/**
	 * Returns a list of units in sight range.
	 * 
	 * @param unit
	 *            Unit to evaluate.
	 * @return list of units in sight range.
	 */
	public static List<Unit> getUnitsInSightRange(Unit unit) {

		Unit u = unit;
		UnitType t = u.getType();

		return unit.getUnitsInRadius(t.sightRange());
	}

	/**
	 * Returns true if the unit is alone, e.g. there are no units (that are not
	 * buildings) in his sightRange.
	 * 
	 * @param unit
	 *            Unit to evaluate.
	 * @return true if the unit is alone.
	 */
	public static boolean isAlone(Unit unit) {
		List<Unit> l = getUnitsInSightRange(unit);

		for (int i = 0; i < l.size(); i++) {
			if (l.get(i).getType() != UnitType.Buildings)
				return false;
		}
		return true;
	}

	/**
	 * Returns true if otherUnit is an ally. Buildings included.
	 * 
	 * @param unit
	 *            Unit to evaluate.
	 * @param otherUnit
	 *            Unit to evaluate.
	 * @return true if otherUnit is an ally.
	 */
	public static boolean isAlly(Unit unit, Unit otherUnit) {
		return unit.getPlayer().isAlly(otherUnit.getPlayer());
	}

	/**
	 * Returns true if otherUnit is an ally. Buildings excluded.
	 * 
	 * @param unit
	 *            Unit to evaluate.
	 * @param otherUnit
	 *            Unit to evaluate.
	 * @return true if otherUnit is an ally and is not a building.
	 */
	public static boolean isAlliedUnit(Unit unit, Unit otherUnit) {
		return unit.getPlayer().isAlly(otherUnit.getPlayer()) && otherUnit.getType() != UnitType.Buildings;
	}

	/**
	 * Returns true if otherUnit is an enemy. Buildings included.
	 * 
	 * @param unit
	 *            Unit to evaluate.
	 * @param otherUnit
	 *            Unit to evaluate.
	 * @return true if otherUnit is an enemy.
	 */
	public static boolean isEnemy(Unit unit, Unit otherUnit) {
		return !unit.getPlayer().isAlly(otherUnit.getPlayer());
	}

	/**
	 * Returns true if otherUnit is an enemy. Buildings excluded.
	 * 
	 * @param unit
	 *            Unit to evaluate.
	 * @param otherUnit
	 *            Unit to evaluate.
	 * @return true if otherUnit is an enemy and is not a building.
	 */
	public static boolean isEnemyUnit(Unit unit, Unit otherUnit) {
		return !unit.getPlayer().isAlly(otherUnit.getPlayer()) && otherUnit.getType() != UnitType.Buildings;
	}

	/**
	 * Returns the list of units in sight that are allies. Buildings included.
	 * 
	 * @param unit
	 *            Unit to evaluate.
	 * @return a list of units containing the units in sight of unit that are
	 *         allies.
	 */
	public static List<Unit> getAlliesAround(Unit unit) {
		List<Unit> units = getUnitsInSightRange(unit);
		List<Unit> r = new ArrayList<>(units.size());

		Unit u;
		int s = units.size();
		for (int i = 0; i < s; i++) {
			u = units.get(i);
			if (isAlly(unit, u))
				r.add(u);
		}

		return r;
	}

	/**
	 * Returns the list of units in sight that are allies. Buildings excluded.
	 * 
	 * @param unit
	 *            Unit to evaluate.
	 * @return a list of units containing the units in sight of unit that are
	 *         allies. Allied buildings will not be included.
	 */
	public static List<Unit> getAlliedUnitsAround(Unit unit) {
		List<Unit> units = getUnitsInSightRange(unit);
		List<Unit> r = new ArrayList<>(units.size());

		Unit u;
		int s = units.size();
		for (int i = 0; i < s; i++) {
			u = units.get(i);
			if (isAlliedUnit(unit, u))
				r.add(u);
		}

		return r;
	}

	/**
	 * Returns the list of units in sight that are enemies. Buildings included.
	 * 
	 * @param unit
	 *            Unit to evaluate.
	 * @return a list of units containing the units in sight of unit that are
	 *         enemies.
	 */
	public static List<Unit> getEnemiesAround(Unit unit) {
		List<Unit> units = getUnitsInSightRange(unit);
		List<Unit> r = new ArrayList<>(units.size());

		Unit u;
		int s = units.size();
		for (int i = 0; i < s; i++) {
			u = units.get(i);
			if (isEnemy(unit, u))
				r.add(u);
		}

		return r;
	}

	/**
	 * Returns the list of units in sight that are enemies. Buildings excluded.
	 * 
	 * @param unit
	 *            Unit to evaluate.
	 * @return a list of units containing the units in sight of unit that are
	 *         enemies. Enemy buildings will not be included.
	 */
	public static List<Unit> getEnemyUnitsAround(Unit unit) {
		List<Unit> units = getUnitsInSightRange(unit);
		List<Unit> r = new ArrayList<>(units.size());

		Unit u;
		int s = units.size();
		for (int i = 0; i < s; i++) {
			u = units.get(i);
			if (isEnemyUnit(unit, u))
				r.add(u);
		}

		return r;
	}

	/**
	 * Returns true if there are allies around. Buildings included.
	 * 
	 * @param unit
	 *            Unit to evaluate.
	 * @return true if there are allies in sight. If there are no units in
	 *         sight, returns false.
	 */
	public static boolean areAlliesAround(Unit unit) {
		// TODO Improve
		return !getAlliesAround(unit).isEmpty();
	}

	/**
	 * Returns true if there are allies around. Buildings excluded.
	 * 
	 * @param unit
	 *            Unit to evaluate.
	 * @return true if there are allies, that are not buildings, in sight. If
	 *         there are no units in sight, returns false.
	 */
	public static boolean areAlliedUnitsAround(Unit unit) {
		// TODO Improve
		return !getAlliedUnitsAround(unit).isEmpty();
	}

	/**
	 * Returns true if there are enemies around. Buildings included.
	 * 
	 * @param unit
	 *            Unit to evaluate.
	 * @return true if there are enemies in sight. If there are no units in
	 *         sight, returns false.
	 */
	public static boolean areEnemiesAround(Unit unit) {
		// TODO Improve
		return !getEnemiesAround(unit).isEmpty();
	}

	/**
	 * Returns true if there are enemies around. Buildings excluded.
	 * 
	 * @param unit
	 *            Unit to evaluate.
	 * @return true if there are enemies, that are not buildings, in sight. If
	 *         there are no units in sight, returns false.
	 */
	public static boolean areEnemyUnitsAround(Unit unit) {
		// TODO Improve
		return !getEnemyUnitsAround(unit).isEmpty();
	}

	/**
	 * Returns the list of units in groundWeaponRange that are enemies.
	 * Buildings included.
	 * 
	 * @param unit
	 *            Unit to evaluate.
	 * @return a list of units containing the units in groundWeaponRange of unit
	 *         that are enemies.
	 */
	public static List<Unit> getEnemiesInGroundRange(Unit unit) {
		UnitType t = unit.getType();
		WeaponType w = t.groundWeapon();

		List<Unit> units = unit.getUnitsInRadius(w.maxRange());
		List<Unit> r = new ArrayList<>(units.size());

		Unit u;
		int s = units.size();
		for (int i = 0; i < s; i++) {
			u = units.get(i);
			if (isEnemy(unit, u))
				r.add(u);
		}

		return r;
	}

	/**
	 * Returns true if there are enemies in ground weapon range. Buildings
	 * included.
	 * 
	 * @param unit
	 *            Unit to evaluate.
	 * @return true if there are enemies in ground weapon range. If there are no
	 *         units in sight, returns false.
	 */
	public static boolean areEnemiesInGroundRange(Unit unit) {
		// TODO Improve
		return !getEnemiesInGroundRange(unit).isEmpty();
	}

	/**
	 * Return if the unit is faster than another unit.
	 * 
	 * @param unit
	 *            Unit to evaluate.
	 * @param anotherUnit
	 *            Unit to evaluate.
	 * @return True if the unit is faster than another unit.
	 */
	public static boolean isFaster(Unit unit, Unit anotherUnit) {
		UnitType unitType = unit.getType();
		UnitType anotherUnitType = anotherUnit.getType();

		return unitType.topSpeed() > anotherUnitType.topSpeed();
	}
}
