package bot.action.singleUnit;

import java.util.List;

import com.Com;

import bot.commonFunctions.CheckAround;
import bwapi.Order;
import bwapi.Unit;
import newAgent.agent.OnEndActionObserver;

public class AttackUnitOnSightLessHP extends GenericUnitAction {

	public AttackUnitOnSightLessHP(OnEndActionObserver agent, Com com, Unit atacante) {
		super(agent, com, atacante, bot.Const.FRAMES_ATTACK, false);
	}

	@Override
	public void executeAction() {
		List<Unit> l = CheckAround.getEnemiesInGroundRange(unit);

		if (!l.isEmpty()) {
			if (!unit.getOrder().equals(Order.AttackUnit)) {
				// get the enemyUnit with less hp
				Unit u = java.util.Collections.min(l, (a, b) -> a.getHitPoints() <= b.getHitPoints() ? -1 : 1);

				this.unit.attack(u);
				super.order = this.unit.getOrder();
			}
		}
	}

	@Override
	public boolean isPossible() {
		// TODO solo válido contra unidades terrestres
		return CheckAround.areEnemiesInGroundRange(unit);
	}
}
