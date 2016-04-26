package bot.action.singleUnit;

import java.util.List;

import com.Com;

import bot.action.GenericAction;
import bot.commonFunctions.CheckAround;
import bwapi.Order;
import bwapi.Unit;
import newAgent.GenericAgent;

public class AttackUnitOnSightLesHP extends GenericAction {

	public AttackUnitOnSightLesHP(GenericAgent agent, Com com, Unit atacante) {
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
