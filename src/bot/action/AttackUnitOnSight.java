package bot.action;

import java.util.List;

import com.Com;

import bwapi.Order;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.WeaponType;

public class AttackUnitOnSight extends GenericAction {

	public AttackUnitOnSight(Com com, Unit atacante, int epoch) {
		super(com, atacante, bot.Const.FRAMES_ATTACK, false);
		super.agentEpochCreate = epoch;
	}

	@Override
	public void executeAction() {
		List<Unit> l = getGroundUnitsInRange();
		if (!l.isEmpty()) {
			if (!unit.getOrder().equals(Order.AttackUnit)) {
				this.unit.attack(l.get(0));
				super.order = this.unit.getOrder();
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
