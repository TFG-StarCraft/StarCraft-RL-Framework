package bot.action.groupUnit.movement;

import com.Com;
import bot.action.groupUnit.MultiSingleUnit;
import bwapi.Unit;
import newAgent.agent.group.MarineGroupAgent;

public abstract class MoveAction extends MultiSingleUnit {

	public MoveAction(MarineGroupAgent agent, Com com, Long maxFramesOfExecuting, boolean specialStart) {
		super(agent, com, maxFramesOfExecuting, specialStart);
	}

	protected abstract bot.action.singleUnit.movement.MoveAction getAction(Unit unit);

	@Override
	public boolean isPossible() {
		return true;
	}

}
