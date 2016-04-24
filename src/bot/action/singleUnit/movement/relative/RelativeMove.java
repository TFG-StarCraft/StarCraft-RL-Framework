package bot.action.singleUnit.movement.relative;

import com.Com;

import bot.action.singleUnit.movement.MoveAction;
import bwapi.Unit;

public abstract class RelativeMove extends MoveAction {

	protected double modulo;
	
	public RelativeMove(Com com, Unit unit) {
		super(com, unit);
	}

	@Override
	public boolean isPossible() {
		return modulo < com.ComData.unit.getType().width() * 2;
	}

}
