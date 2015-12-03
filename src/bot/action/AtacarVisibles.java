package bot.action;

import java.security.InvalidParameterException;
import java.util.List;

import com.Com;

import bwapi.Order;
import bwapi.Unit;
import bwapi.WeaponType;

public class AtacarVisibles extends GenericAction {

	public AtacarVisibles(Com com, Unit atacante) {
		super(com, atacante, bot.Const.FRAMES_ATTACK, false);
	}

	@Override
	public void executeAction() {
		List<Unit> l = this.unit.getUnitsInWeaponRange(WeaponType.Gauss_Rifle);
		if (!l.isEmpty()) {
			if (!unit.getOrder().equals(Order.AttackUnit)) {
				this.unit.attack(l.get(0));
			}
		}
	}

	@Override
	public void onEndAction(boolean correct) {
		unRegisterUnitObserver();

		if (correct) {
			// TODO
			throw new InvalidParameterException();
		} else {
			// com.onDebugMessage("Miss");
			com.ComData.lastActionOk = false;
			com.ComData.unit.removeAction();
			com.Sync.signalActionEnded();
		}
	}

	@Override
	public boolean isPossible() {
		List<Unit> l = this.unit.getUnitsInWeaponRange(WeaponType.Gauss_Rifle);
		return !l.isEmpty();
	}
}
