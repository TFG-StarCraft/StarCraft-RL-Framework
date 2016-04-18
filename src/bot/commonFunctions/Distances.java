package bot.commonFunctions;

import bwapi.Unit;

/**
 * Set of functions to measure distances relatives to BWAPI units
 * 
 * @author Alberto Casas Ortiz
 * @author Raúl Martín Guadaño
 * @author Miguel Ascanio Gómez
 */
public class Distances {

	/**
	 * Returns the distance from unit to the nearest enemy in sight.
	 * 
	 * @param unit
	 * @return the distance from unit to the nearest enemy. 0 if no enemies are
	 *         in sight.
	 */
	public static Double getDistanceToNearestEnemy(Unit unit) {
		return CheckAround.getEnemiesAround(unit).stream().map(u -> {
			double vX = u.getX() - unit.getX();
			double vY = u.getY() - unit.getY();
			return Math.sqrt(vX * vX + vY * vY);
		}).min(Double::compareTo).orElse(0.0);
	}

}
