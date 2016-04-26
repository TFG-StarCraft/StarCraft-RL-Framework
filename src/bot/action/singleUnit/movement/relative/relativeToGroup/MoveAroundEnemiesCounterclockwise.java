package bot.action.singleUnit.movement.relative.relativeToGroup;

import java.util.List;

import com.Com;

import bot.action.singleUnit.movement.MoveAction;
import bot.action.singleUnit.movement.relative.ClokwiseMove;
import bot.commonFunctions.CheckAround;
import bwapi.Unit;
import newAgent.GenericAgent;

public class MoveAroundEnemiesCounterclockwise extends MoveAction implements ClokwiseMove {

	public MoveAroundEnemiesCounterclockwise(GenericAgent agent, Com com, Unit unit) {
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
		List<Unit> l = CheckAround.getEnemiesAround(unit);
		if (!l.isEmpty()) {
			double pX = 0, pY = 0;
			int cont = 0;
			// Calculate point between enemies.
			for (int i = 0; i < l.size(); i++) {
				pX += l.get(i).getX();
				pY += l.get(i).getY();
				cont++;
			}
			pX /= cont;
			pY /= cont;

			unit.getX();
			unit.getY();

			this.endX += (int) Math.floor((unit.getX() - pX) * c + (unit.getY() - pY) * s) + pX;
			this.endY += (int) Math.floor(-(unit.getX() - pX) * s + (unit.getY() - pY) * c) + pY;

		} else {
			this.endX = unit.getX();
			this.endY = unit.getY();
		}
	}

}
