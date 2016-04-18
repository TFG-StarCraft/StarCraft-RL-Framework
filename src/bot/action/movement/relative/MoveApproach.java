package bot.action.movement.relative;

import java.util.List;

import com.Com;

import bot.commonFunctions.CheckAround;
import bwapi.Unit;

/**
 * Movement. Approaching to a target unit.
 * 
 * @author Alberto Casas Ortiz
 * @author Raúl Martín Guadaño
 * @author Miguel Ascanio Gómez
 */
public class MoveApproach extends RelativeMove {

	/***************/
	/* CONSTRUCTOR */
	/***************/

	/**
	 * Constructor of the class MoveApproach.
	 * 
	 * @param com
	 *            Comunication.
	 * @param unit
	 *            Unit to move.
	 * @param agentEpoch
	 */
	public MoveApproach(Com com, Unit unit) {
		super(com, unit);
	}

	/*******************/
	/* OVERRIDE METHOD */
	/*******************/

	/**
	 * Do the approach movement.
	 */
	@Override
	protected void setUpMove() {
		List<Unit> l = CheckAround.getUnitsInSightRange(unit);
		if (!l.isEmpty()) {
			Unit u = l.get(0);
			// Calculate vector from unit to target.
			double vX = u.getX() - unit.getX();
			double vY = u.getY() - unit.getY();

			modulo = Math.sqrt(vX * vX + vY * vY);
			vX /= modulo;
			vY /= modulo;

			// Advance step from target to target.
			this.endX = unit.getX() + (int) Math.ceil(-vX * bot.Const.STEP);
			this.endY = unit.getY() + (int) Math.ceil(-vY * bot.Const.STEP);

		}
		// Otherwise, do nothing.
		else {
			this.endX = unit.getX();
			this.endY = unit.getY();
		}
	}

}
