package bot.action.movement.relative;

import java.util.List;

import com.Com;

import bot.action.movement.MoveAction;
import bot.commonFunctions.CheckAround;
import bwapi.Unit;

public class MoveAroundClockwise extends MoveAction implements ClokwiseMove {

	public MoveAroundClockwise(Com com, Unit unit) {
		super(com, unit);
	}

	/*******************/
	/* OVERRIDE METHOD */
	/*******************/

	/**
	 * Do the moveArountRight movement.
	 */
	@Override
	protected void setUpMove() {
		List<Unit> l = CheckAround.getUnitsInSightRange(unit);
		if (!l.isEmpty()) {
			Unit u = l.get(0);

			this.endX += (int) Math.floor((unit.getX() - u.getX()) * c - (unit.getY() - u.getY()) * s) + u.getX();
			this.endY += (int) Math.floor((unit.getX() - u.getX()) * s + (unit.getY() - u.getY()) * c) + u.getY();

		} else {
			this.endX = unit.getX();
			this.endY = unit.getY();
		}
	}

}
