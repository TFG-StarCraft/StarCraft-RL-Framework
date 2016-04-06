package bot.action.relativeMovement;

import com.Com;

import bot.action.movement.MoveAction;
import bwapi.Unit;

public abstract class RelativeMove extends MoveAction{

	protected double modulo;
	
	public RelativeMove(Com com, Unit unit) {
		super(com, unit);
	}

}
