package bot.action.movement;

import java.util.List;

import com.Com;

import bwapi.Unit;
import bwapi.UnitType;
import bwapi.WeaponType;

/**
 * Movement. Approaching to a target unit.
 * @author Alberto Casas Ortiz.
 * @author Raúl Martín Guadaño
 * @author Miguel Ascanio Gómez.
 */
public class MoveApproach extends MoveAction{
	
	
	/***************/
	/* CONSTRUCTOR */
	/***************/
	
	/**
	 * Constructor of the class MoveApproach.
	 * @param com Comunication.
	 * @param unit Unit to move.
	 * @param agentEpoch 
	 */
	public MoveApproach(Com com, Unit unit, int agentEpoch) {
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
		WeaponType w = t.groundWeapon();
		
		return this.unit.getUnitsInRadius(w.maxRange());
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
			
			double mov = Math.sqrt(vX * bot.Const.STEP + vY * bot.Const.STEP);
			
			if (mov < modulo) {
				//Advance step from target to target.
				this.endX = unit.getX() + (int) Math.ceil(vX * bot.Const.STEP);
				this.endY = unit.getY() + (int) Math.ceil(vY * bot.Const.STEP);
			} else {
				// Stay in position
				this.endX = unit.getX();
				this.endY = unit.getY();
			}
		}
		//Otherwise, do nothing.
		else{
			this.endX = unit.getX();
			this.endY = unit.getY();
		}
	}

	
	
}
