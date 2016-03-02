package bot.action.movement;

import java.util.List;

import com.Com;

import bwapi.Unit;
import bwapi.UnitType;

public class MoveAroundLeft extends MoveAction{

	public MoveAroundLeft(Com com, Unit unit, int agentEpoch) {
		super(com, unit, agentEpoch);
	}

	
	
	/******************/
	/* PRIVATE METHOD */
	/******************/
	
	/**
	 * Get the units in the range of the moving unit.
	 * @return A list with the units in the range.
	 */
	private List<Unit> getGroundUnitsInRange() {
		
		Unit u = this.unit;
		UnitType t = u.getType();
		
		return this.unit.getUnitsInRadius(t.sightRange());
	}

	
	
	/*******************/
	/* OVERRIDE METHOD */
	/*******************/
	
	/**
	 * Do the moveArountRight movement.
	 */
	@Override
	protected void setUpMove() {
		List<Unit> l = getGroundUnitsInRange();
		if (!l.isEmpty()) {
			Unit u = l.get(0);

			double s = Math.sin(Math.PI/16);
			double c = Math.cos(Math.PI/16);
			
			
			this.endX += (int) Math.floor((unit.getX() - u.getX()) * c + (unit.getY() - u.getY()) * s) + u.getX();
			this.endY += (int) Math.floor(- (unit.getX() - u.getX()) * s + (unit.getY() - u.getY()) * c) + u.getY();

		}
		else{
			this.endX = unit.getX();
			this.endY = unit.getY();
		}
	}

	
}
