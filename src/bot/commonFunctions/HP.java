package bot.commonFunctions;

import java.util.List;

import bwapi.Unit;
import bwapi.WeaponType;

/**
 * Set of functions which help to determine the HP of the units in the
 * environment.
 * 
 * @author Alberto Casas Ortiz
 * @author Raúl Martín Guadaño
 * @author Miguel Ascanio Gómez
 */
public class HP {
	/**
	 * Returns the sum of the hit points of all enemies around.
	 * 
	 * @param unit
	 * @return the sum of the hp of all enemies in sight range of unit. -1 if no
	 *         enemies are found.
	 */
	public static double getHPOfEnemiesAround(Unit unit) {
		List<Unit> l = CheckAround.getEnemiesAround(unit);
		if (l.isEmpty())
			return -1;

		double r = 0.0;
		for (int i = 0; i < l.size(); i++) {
			r += l.get(i).getHitPoints();
		}
		return r;
	}

	/**
	 * Return if the unit can beat another unit, both with ground weapons.
	 * 
	 * @param unit
	 *            Unit to evaluate.
	 * @param anotherUnit
	 *            Unit to evaluate.
	 * @return True if the unit can beat another unit.
	 */
	public static boolean canBeatGround(Unit unit, Unit anotherUnit) {
		WeaponType unitWeapon = unit.getType().groundWeapon();
		WeaponType anotherUnitWeapon = unit.getType().groundWeapon();

		double hurtedUnit = unit.getHitPoints() / anotherUnitWeapon.damageAmount();
		double hurtedAnotherUnit = anotherUnit.getHitPoints() / unitWeapon.damageAmount();

		double timeDeadUnit = unit.getHitPoints() * anotherUnitWeapon.damageCooldown() / hurtedUnit;
		double timeDeadAnotherUnit = anotherUnit.getHitPoints() * unitWeapon.damageCooldown() / hurtedAnotherUnit;

		return timeDeadUnit > timeDeadAnotherUnit;
	}

	/**
	 * Return if the unit can beat another unit, both with ground weapons.
	 * 
	 * @param unit
	 *            Unit to evaluate.
	 * @param anotherUnit
	 *            Unit to evaluate.
	 * @return True if the unit can beat another unit.
	 */
	public static boolean canBeatAir(Unit unit, Unit anotherUnit) {
		WeaponType unitWeapon = unit.getType().airWeapon();
		WeaponType anotherUnitWeapon = unit.getType().airWeapon();

		double hurtedUnit = unit.getHitPoints() / anotherUnitWeapon.damageAmount();
		double hurtedAnotherUnit = anotherUnit.getHitPoints() / unitWeapon.damageAmount();

		double timeDeadUnit = unit.getHitPoints() * anotherUnitWeapon.damageCooldown() / hurtedUnit;
		double timeDeadAnotherUnit = anotherUnit.getHitPoints() * unitWeapon.damageCooldown() / hurtedAnotherUnit;

		return timeDeadUnit > timeDeadAnotherUnit;
	}
}
