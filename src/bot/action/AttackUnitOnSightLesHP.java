package bot.action;

import java.util.List;
import java.util.Optional;

import com.Com;

import bwapi.Order;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.WeaponType;

public class AttackUnitOnSightLesHP extends GenericAction {

	public AttackUnitOnSightLesHP(Com com, Unit atacante) {
		super(com, atacante, bot.Const.FRAMES_ATTACK, false);
	}

	@Override
	public void executeAction() {
		List<Unit> l = getGroundUnitsInRange();
//		boolean found = false;
//		int i = 0;
		if (!l.isEmpty()) {
			if (!unit.getOrder().equals(Order.AttackUnit)) {
				// get the enemyUnit with less hp
				Optional<Unit> o = l.stream().filter(u -> !u.getPlayer().isAlly(unit.getPlayer()))
						.min((a, b) -> a.getHitPoints() < b.getHitPoints() ? -1 : 1);
				if (o.isPresent()) {
					this.unit.attack(o.get());
					super.order = this.unit.getOrder();	
				}
//				while (!found && i < l.size()) {
//					if (!l.get(i).getPlayer().isAlly(unit.getPlayer())) {
//						found = true;
//						this.unit.attack(l.get(i));
//						super.order = this.unit.getOrder();
//					}
//					i++;
//				}

			}
		}
	}

	@Override
	public boolean isPossible() {
		// TODO solo válido contra unidades terrestres
		List<Unit> l = getGroundUnitsInRange();
		return !l.isEmpty();
	}

	private List<Unit> getGroundUnitsInRange() {

		Unit u = this.unit;
		UnitType t = u.getType();
		WeaponType w = t.groundWeapon();

		return this.unit.getUnitsInRadius(w.maxRange());
	}
}
