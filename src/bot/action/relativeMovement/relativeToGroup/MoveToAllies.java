package bot.action.relativeMovement.relativeToGroup;

import java.util.List;

import com.Com;

import bot.action.movement.MoveAction;
import bwapi.Unit;
import bwapi.UnitType;

/**
 * Movement. Approaching to a target unit.
 * @author Alberto Casas Ortiz
 * @author Raúl Martín Guadaño
 * @author Miguel Ascanio Gómez
 */
public class MoveToAllies extends MoveAction {
	
	
	/***************/
	/* CONSTRUCTOR */
	/***************/
	
	/**
	 * Constructor of the class MoveApproach.
	 * @param com Comunication.
	 * @param unit Unit to move.
	 * @param agentEpoch 
	 */
	public MoveToAllies(Com com, Unit unit) {
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
			double pX = 0, pY = 0;
			int cont = 0;
			//Calculate point between allies.
			for(int i = 0; i < l.size(); i++){
				if(l.get(i).getPlayer().isAlly(unit.getPlayer())){
					pX += l.get(i).getX();
					pY += l.get(i).getY();
					cont++;
				}
			}
			pX /= cont;
			pY /= cont;
			
			//Calculate vector to point.
			double vX = pX - unit.getX();
			double vY = pY - unit.getY();
			
			modulo = Math.sqrt(vX*vX + vY*vY);
			
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

	@Override
	public boolean isPossible() {
		return modulo < com.ComData.unit.getType().width() * 2;
	}
	
}
