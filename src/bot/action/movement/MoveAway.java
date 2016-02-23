package bot.action.movement;

import java.util.List;

import com.Com;

import bwapi.Unit;
import bwapi.UnitType;
import bwapi.WeaponType;

public class MoveAway extends MoveAction{

	public MoveAway(Com com, Unit unit, int agentEpoch) {
		super(com, unit, agentEpoch);
	}

	@Override
	protected void setUpMove() {
		List<Unit> l = getGroundUnitsInRange();
		if (!l.isEmpty()) {
			Unit u = l.get(0);
			double vX = u.getX() - unit.getX();
			double vY = u.getY() - unit.getY();
			double modulo = Math.sqrt(vX*vX + vY*vY);
			vX /= modulo;
			vY /= modulo;
			
			this.endX = unit.getX() + (int) Math.ceil(-vX * bot.Const.STEP);
			this.endY = unit.getY() + (int) Math.ceil(-vY * bot.Const.STEP);
			
			

		}
		//Si no, no hacemos nada
		else{
			this.endX = unit.getX();
			this.endY = unit.getY();
		}
	}


	
	private List<Unit> getGroundUnitsInRange() {
		
		Unit u = this.unit;
		UnitType t = u.getType();
		WeaponType w = t.groundWeapon();
		
		return this.unit.getUnitsInRadius(w.maxRange());
	}
}
