package bot.action.singleUnit.movement.relative;

import com.Com;

import bot.action.singleUnit.movement.MoveAction;
import bwapi.Unit;
import newAgent.GenericAgent;

public abstract class RelativeMove extends MoveAction {

	protected double modulo;
	
	public RelativeMove(GenericAgent agent, Com com, Unit unit) {
		super(agent, com, unit);
	}	

	@Override
	public boolean isPossible() {
		return modulo < unit.getType().width() * 2;
	}

}
