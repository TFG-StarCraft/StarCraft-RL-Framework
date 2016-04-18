package bot.action;

import java.util.List;
import java.util.Optional;

import com.Com;

import bot.commonFunctions.CheckAround;
import bwapi.Order;
import bwapi.Unit;

public class AttackUnitOnSightLesHP extends GenericAction {

	public AttackUnitOnSightLesHP(Com com, Unit atacante) {
		super(com, atacante, bot.Const.FRAMES_ATTACK, false);
	}

	@Override
	public void executeAction() {
		List<Unit> l = CheckAround.getEnemiesInGroundRange(unit);

		if (!l.isEmpty()) {
			if (!unit.getOrder().equals(Order.AttackUnit)) {
				// get the enemyUnit with less hp
				Optional<Unit> o = l.stream().min((a, b) -> a.getHitPoints() < b.getHitPoints() ? -1 : 1);
				if (o.isPresent()) {
					this.unit.attack(o.get());
					super.order = this.unit.getOrder();
				}
			}
		}
	}

	@Override
	public boolean isPossible() {
		// TODO solo válido contra unidades terrestres
		return CheckAround.areEnemiesAround(unit);
	}
}
