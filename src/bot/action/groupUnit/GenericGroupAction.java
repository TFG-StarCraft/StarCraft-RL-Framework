package bot.action.groupUnit;

import com.Com;

import bot.action.GenericAction;
import newAgent.agent.group.MarineGroupAgent;
import utils.DebugEnum;

public abstract class GenericGroupAction extends GenericAction {

	protected MarineGroupAgent groupAgent;
	
	public GenericGroupAction(MarineGroupAgent agent, Com com, Long maxFramesOfExecuting, boolean specialStart) {
		super(agent, com, maxFramesOfExecuting, specialStart);
		this.groupAgent = agent;
	}

	@Override
	public abstract void executeAction();

	@Override
	public abstract boolean isPossible();

	@Override
	public void onFrame() {
		if (actionStarted && isFramesLimitsReached()) {
			com.onDebugMessage("End - frame limit" + this.getClass(), DebugEnum.FRAME_LIMIT);
			onEndAction(false);
		} else {
			if (!specialStart) {
				startAction();
			}
			executeAction();
//			if (actionStarted && order != null && order != unit.getOrder()) {
//				com.onDebugMessage("End - Bad order" + (order == null ? ", null" : ", not equals"), DebugEnum.BAD_ORDER);
//				onEndAction(false);
//			}
		}
	}

}
