package bot.action.singleUnit.movement.relative.relativeToGroup;

import java.util.List;

import com.Com;

import bot.action.singleUnit.movement.relative.RelativeMove;
import bot.commonFunctions.CheckAround;
import bwapi.Unit;

/**
 * Movement. Approaching to a target unit.
 * @author Alberto Casas Ortiz
 * @author Raúl Martín Guadaño
 * @author Miguel Ascanio Gómez
 */
public class MoveToAllies extends RelativeMove {
	
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
	
	/*******************/
	/* OVERRIDE METHOD */
	/*******************/
	
	/**
	 * Do the approach movement.
	 */
	@Override
	protected void setUpMove() {
		List<Unit> l = CheckAround.getAlliesAround(unit);
		if (!l.isEmpty()) {
			double pX = 0, pY = 0;
			int cont = 0;
			//Calculate point between allies.
			for(int i = 0; i < l.size(); i++){
				pX += l.get(i).getX();
				pY += l.get(i).getY();
				cont++;
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
}
