package bot.action.singleUnit.movement.relative;

import java.util.List;

import com.Com;

import bot.action.singleUnit.movement.MoveAction;
import bot.commonFunctions.CheckAround;
import bwapi.Unit;
import newAgent.GenericAgent;

public class MoveAroundClockwise extends MoveAction implements ClokwiseMove {

	public MoveAroundClockwise(GenericAgent agent, Com com, Unit unit) {
		super(agent, com, unit);
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
