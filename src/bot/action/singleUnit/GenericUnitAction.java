package bot.action.singleUnit;

import com.Com;

import bot.action.GenericAction;
import bwapi.Unit;
import newAgent.agent.GenericAgent;
import utils.DebugEnum;

public abstract class GenericUnitAction extends GenericAction {

	protected final Unit unit;	
	
	public GenericUnitAction(GenericAgent agent, Com com, Unit unit, Long maxFramesOfExecuting, boolean specialStart) {
		super(agent, com, maxFramesOfExecuting, specialStart);
		
		this.unit = unit;
	}

	public void onFrame() {
		if (actionStarted && isFramesLimitsReached()) {
			com.onDebugMessage("End - frame limit" + this.getClass(), DebugEnum.FRAME_LIMIT);
			onEndAction(false);
		} else {
			if (!specialStart) {
				startAction();
			}
			executeAction();
			if (actionStarted && order != null && order != unit.getOrder()) {
				com.onDebugMessage("End - Bad order" + (order == null ? ", null" : ", not equals"), DebugEnum.BAD_ORDER);
				onEndAction(false);
			}
		}
	}
}
