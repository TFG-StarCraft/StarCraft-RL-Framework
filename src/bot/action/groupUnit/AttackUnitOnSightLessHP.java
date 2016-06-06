package bot.action.groupUnit;

import com.Com;

import bot.action.singleUnit.GenericUnitAction;
import bot.commonFunctions.CheckAround;
import bwapi.Unit;
import newAgent.agent.group.MarineGroupAgent;

public class AttackUnitOnSightLessHP extends MultiSingleUnit {

	public AttackUnitOnSightLessHP(MarineGroupAgent agent, Com com) {
		super(agent, com, bot.Const.FRAMES_ATTACK, false);
	}

	@Override
	protected GenericUnitAction getAction(Unit unit) {
		return new bot.action.singleUnit.AttackUnitOnSightLessHP(this, com, unit);
	}

	@Override
	public boolean isPossible() {
		for (Unit unit : units) {
			if (CheckAround.areEnemiesInGroundRange(unit)) {
				return true;
			}
		}
		return false;
	}

}
