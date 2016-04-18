package bot.action.movement.relative.relativeToGroup;

import java.util.List;

import com.Com;

import bot.action.movement.MoveAction;
import bot.action.movement.relative.ClokwiseMove;
import bot.commonFunctions.CheckAround;
import bwapi.Unit;

public class MoveAroundAlliesClockwise extends MoveAction implements ClokwiseMove {

	public MoveAroundAlliesClockwise(Com com, Unit unit) {
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
		List<Unit> l = CheckAround.getAlliesAround(unit);
		if (!l.isEmpty()) {
			double pX = 0, pY = 0;
			int cont = 0;
			// Calculate point between allies.
			for (int i = 0; i < l.size(); i++) {
				pX += l.get(i).getX();
				pY += l.get(i).getY();
				cont++;
			}
			pX /= cont;
			pY /= cont;

			unit.getX();
			unit.getY();

			this.endX += (int) Math.floor((unit.getX() - pX) * c - (unit.getY() - pY) * s) + pX;
			this.endY += (int) Math.floor((unit.getX() - pX) * s + (unit.getY() - pY) * c) + pY;

		} else {
			this.endX = unit.getX();
			this.endY = unit.getY();
		}
	}

}
