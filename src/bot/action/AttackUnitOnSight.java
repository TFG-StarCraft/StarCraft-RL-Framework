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
		boolean found = false;
		int i = 0;
		if (!l.isEmpty()) {
			if (!unit.getOrder().equals(Order.AttackUnit)) {
				while(!found && i < l.size()){
						if(!l.get(i).getPlayer().isAlly(unit.getPlayer())){
							found = true;
							this.unit.attack(l.get(i));
							super.order = this.unit.getOrder();
						}
						i++;
				}
				
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
