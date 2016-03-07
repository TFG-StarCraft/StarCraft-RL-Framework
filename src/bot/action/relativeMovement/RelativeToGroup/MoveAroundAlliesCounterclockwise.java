package bot.action.relativeMovement.RelativeToGroup;

import java.util.List;

import com.Com;

import bot.action.movement.MoveAction;
import bwapi.Unit;
import bwapi.UnitType;

public class MoveAroundAlliesCounterclockwise extends MoveAction {

	public MoveAroundAlliesCounterclockwise(Com com, Unit unit) {
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
	 * Do the moveArountRight movement.
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
			
			unit.getX();
			unit.getY();
			
			double s = Math.sin(Math.PI/16);
			double c = Math.cos(Math.PI/16);
			
			
			this.endX += (int) Math.floor((unit.getX() - pX) * c + (unit.getY() - pY) * s) + pX;
			this.endY += (int) Math.floor(- (unit.getX() - pX) * s + (unit.getY() - pY) * c) + pY;

		}
		else{
			this.endX = unit.getX();
			this.endY = unit.getY();
		}
	}

	
}
