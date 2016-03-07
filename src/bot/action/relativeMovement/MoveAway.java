package bot.action.relativeMovement;

import java.util.List;

import com.Com;

import bot.action.movement.MoveAction;
import bwapi.Unit;
import bwapi.UnitType;

/**
 * Movement. Moving away from a target unit.
 * @author Alberto Casas Ortiz.
 * @author Raúl Martín Guadaño
 * @author Miguel Ascanio Gómez.
 */
public class MoveAway extends MoveAction {	
	
	/***************/
	/* CONSTRUCTOR */
	/***************/
	
	/**
	 * Constructor of the class MoveAway.
	 * @param com Comunication.
	 * @param unit Unit to move.
	 */
	public MoveAway(Com com, Unit unit) {
		super(com, unit);
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
	 * Do the approach movement.
	 */
	@Override
	protected void setUpMove() {
		List<Unit> l = getGroundUnitsInRange();
		if (!l.isEmpty()) {
			Unit u = l.get(0);
			//Calculate vector from unit to target.
			double vX = u.getX() - unit.getX();
			double vY = u.getY() - unit.getY();
			double modulo = Math.sqrt(vX*vX + vY*vY);
			vX /= modulo;
			vY /= modulo;

			//Advance step from target to target.
			this.endX = unit.getX() + (int) Math.ceil(vX * bot.Const.STEP);
			this.endY = unit.getY() + (int) Math.ceil(vY * bot.Const.STEP);
			
			

		}
		//Otherwise, do nothing.
		else{
			this.endX = unit.getX();
			this.endY = unit.getY();
		}
	}

	
	
}
