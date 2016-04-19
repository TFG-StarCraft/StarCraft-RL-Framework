package bot.commonFunctions;

import java.util.List;

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
		List<Unit> l = CheckAround.getEnemiesAround(unit);
		
		int s = l.size();
		Unit u;
		double vX, vY, dist;
		double min = Double.POSITIVE_INFINITY;
		for (int i = 0; i < s; i++) {
			u = l.get(i);
			vX = u.getX() - unit.getX();
			vY = u.getY() - unit.getY();
			if ((dist = Math.abs(Math.sqrt(vX * vX + vY * vY))) < min) {
				min = dist;
			}
		}
		
		return Double.isInfinite(min) ? 0.0 : (min);
	}

}
