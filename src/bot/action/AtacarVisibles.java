package bot.action;

import java.util.List;

import com.Com;

import bot.observers.OnUnitDestroyObserver;
import bwapi.Order;
import bwapi.Unit;
import bwapi.WeaponType;
import bot.event.Event;

public class AtacarVisibles implements GenericAction, OnUnitDestroyObserver {

	protected final Unit unit;

	private Com com;
	private long frameEnd;
	
	public AtacarVisibles(Com com, Unit atacante) {
		this.unit = atacante;
		this.com = com;
		
		this.frameEnd = com.bot.frames + 13;
	}

	@Override
	public void checkAndActuate() {
		if (com.bot.frames >= frameEnd) {
			onEndAction(false);
		} else {
			List<Unit> l = this.unit.getUnitsInWeaponRange(WeaponType.Gauss_Rifle);
			if (!l.isEmpty()) {
				if (!unit.getOrder().equals(Order.AttackUnit)) {
					this.unit.attack(l.get(0));
				}
			}
		}
	}

	@Override
	public void onEndAction(boolean correct) {
		if (correct) {
			com.ComData.action = null;
			com.ComData.lastActionOk = true;
			com.ComData.unit.removeAction();
			com.Sync.s_postAction.release();
		} else {
			// System.out.println("Miss");
			com.ComData.action = null;
			com.ComData.lastActionOk = false;
			com.ComData.unit.removeAction();
			com.Sync.s_postAction.release();
		}
	}

	@Override
	public boolean isPossible() {
		List<Unit> l = this.unit.getUnitsInWeaponRange(WeaponType.Gauss_Rifle);
		return !l.isEmpty();
	}

	@Override
	public void onUnitDestroy(Unit unit) {
		if (unit.equals(this.unit)) {
			com.bot.addEvent(new Event(Event.CODE_KILLED));
		} else {
			com.bot.addEvent(new Event(Event.CODE_KILL));
		}
		onEndAction(true);
		
	}

	@Override
	public void register() {
		this.com.bot.registerOnUnitDestroyObserver(this);
	}

}
