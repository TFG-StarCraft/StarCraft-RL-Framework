package bot.checkEnviroment;

import java.util.List;

import bwapi.Unit;
import bwapi.UnitType;
import bwapi.WeaponType;

/**
 * Set of functions which help to determine the features of the units in the enviroment.
 * 
 * @author Alberto Casas Ortiz
 * @author Raul Martin Guadaño
 * @author Miguel Ascanio Gomez
 */
public class checkAround {
	
	/**
	 * Return if the unit is alone, e.g., if there are units different to buildings.
	 * @param unit Unit to evaluate.
	 * @return True if the unit is allone.
	 */
	public static boolean isAlone(Unit unit){
		boolean res = true;
		List<Unit> l = getUnitsInSightRange(unit);
		for(int i = 0; i < l.size(); i++){
			if(l.get(i).getType() == UnitType.Buildings)
				res = false;
		}
		return res;
	}
	
	/**
	 * Return if another unit is an ally.
	 * @param unit Unit to evaluate.
	 * @param otherUnit Unit to evaluate.
	 * @return True if another unit is an ally.
	 */
	public static boolean isAlly(Unit unit, Unit otherUnit){
		return unit.getPlayer().isAlly(otherUnit.getPlayer());
	}
	
	/**
	 * Return if there are allies around. Also applied to buildings.
	 * @param unit Unit to evaluate.
	 * @return True if there are allies.
	 */
	public static boolean areAlliesAround(Unit unit){
		List<Unit> l = getUnitsInSightRange(unit);
		boolean res = false;
		for(int i = 0; i < l.size() && !res; i++){
			if(isAlly(unit, l.get(i)));
				res = true;
		}
		return res;
	}
	
	/**
	 * Return if there are allies around.
	 * @param unit Unit to evaluate.
	 * @return True if there are allies.
	 */
	public static boolean areUnitAlliesAround(Unit unit){
		List<Unit> l = getUnitsInSightRange(unit);
		boolean res = false;
		for(int i = 0; i < l.size() && !res; i++){
			if(isAlly(unit, l.get(i)) && l.get(i).getType() != UnitType.Buildings);
				res = true;
		}
		return res;
	}
	
	/**
	 * Return if there are enemies around. Also applied to buildings.
	 * @param unit Unit to evaluate.
	 * @return True if there are enemies.
	 */
	public static boolean areEnemiesAround(Unit unit){
		List<Unit> l = getUnitsInSightRange(unit);
		boolean res = false;
		for(int i = 0; i < l.size() && !res; i++){
			if(!isAlly(unit, l.get(i)))
				res = true;
		}
		return res;
	}
	
	/**
	 * Return if there are enemies around.
	 * @param unit Unit to evaluate.
	 * @return True if there are enemies.
	 */
	public static boolean areUnitEnemiesAround(Unit unit){
		List<Unit> l = getUnitsInSightRange(unit);
		boolean res = false;
		for(int i = 0; i < l.size() && !res; i++){
			if(!isAlly(unit, l.get(i)) && l.get(i).getType() != UnitType.Buildings)
				res = true;
		}
		return res;
	}
	
	/**
	 * Return if there are more allies around than enemies, also applied to buildings.
	 * @param unit Unit to evaluate.
	 * @return True if there are more allies than enemies.
	 */
	public static boolean areMoreAlliesThanEnemies(Unit unit){
		List<Unit> l = getUnitsInSightRange(unit);
		boolean res = false;
		int allies = 0;
		for(int i = 0; i < l.size() && !res; i++){
			if(isAlly(unit, l.get(i)))
				allies++;
		}
		return allies>l.size();
	}
	
	/**
	 * Return if there are more allies around than enemies.
	 * @param unit Unit to evaluate.
	 * @return True if there are more allies than enemies.
	 */
	public static boolean areMoreUnitAlliesThanEnemies(Unit unit){
		List<Unit> l = getUnitsInSightRange(unit);
		boolean res = false;
		int allies = 0;
		int enemies = 0;
		for(int i = 0; i < l.size() && !res; i++){
			if(isAlly(unit, l.get(i)) && l.get(i).getType() != UnitType.Buildings)
				allies++;
			else if(l.get(i).getType() != UnitType.Buildings)
				enemies++;
		}
		return allies>enemies;
	}
	
	/**
	 * Return if there are units in attack range of the weapon.
	 * @param unit Unit to evaluate.
	 * @return True if there are units.
	 */
	public static List<Unit> getGroundUnitsInAttackRange(Unit unit) {

		Unit u = unit;
		UnitType t = u.getType();
		WeaponType w = t.groundWeapon();

		return unit.getUnitsInRadius(w.maxRange());
	}

	/**
	 * Return if the unit can beat another unit, both with ground weapons.
	 * @param unit Unit to evaluate.
	 * @param anotherUnit Unit to evaluate.
	 * @return True if the unit can beat another unit.
	 */
	public static boolean canBeatGround(Unit unit, Unit anotherUnit){
		WeaponType unitWeapon = unit.getType().groundWeapon();
		WeaponType anotherUnitWeapon = unit.getType().groundWeapon();
		
		double hurtedUnit = unit.getHitPoints()/anotherUnitWeapon.damageAmount();
		double hurtedAnotherUnit = anotherUnit.getHitPoints()/unitWeapon.damageAmount();
		
		double timeDeadUnit = unit.getHitPoints()*anotherUnitWeapon.damageCooldown()/hurtedUnit;
		double timeDeadAnotherUnit = anotherUnit.getHitPoints()*unitWeapon.damageCooldown()/hurtedAnotherUnit;
		
		return timeDeadUnit > timeDeadAnotherUnit;
	}

	/**
	 * Return if the unit can beat another unit, both with ground weapons.
	 * @param unit Unit to evaluate.
	 * @param anotherUnit Unit to evaluate.
	 * @return True if the unit can beat another unit.
	 */
	public static boolean canBeatAir(Unit unit, Unit anotherUnit){
		WeaponType unitWeapon = unit.getType().airWeapon();
		WeaponType anotherUnitWeapon = unit.getType().airWeapon();
		
		double hurtedUnit = unit.getHitPoints()/anotherUnitWeapon.damageAmount();
		double hurtedAnotherUnit = anotherUnit.getHitPoints()/unitWeapon.damageAmount();
		
		double timeDeadUnit = unit.getHitPoints()*anotherUnitWeapon.damageCooldown()/hurtedUnit;
		double timeDeadAnotherUnit = anotherUnit.getHitPoints()*unitWeapon.damageCooldown()/hurtedAnotherUnit;
		
		return timeDeadUnit > timeDeadAnotherUnit;
	}
	
	/**
	 * Return if there are units in sight range.
	 * @param unit Unit to evaluate.
	 * @return True if there are units.
	 */
	public static List<Unit> getUnitsInSightRange(Unit unit) {

		Unit u = unit;
		UnitType t = u.getType();

		return unit.getUnitsInRadius(t.sightRange());
	}

	/**
	 * Return if the unit is faster than another unit.
	 * @param unit Unit to evaluate.
	 * @param anotherUnit Unit to evaluate.
	 * @return True if the unit is faster than another unit.
	 */
	public static boolean isFaster(Unit unit, Unit anotherUnit){
		UnitType unitType = unit.getType();
		UnitType anotherUnitType = anotherUnit.getType();
		
		return unitType.topSpeed()>anotherUnitType.topSpeed();
	}
}
