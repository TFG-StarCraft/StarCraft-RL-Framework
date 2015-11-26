package bot.action;

import java.util.List;

import com.Com;

import bot.observers.UnitDestroyObserver;
import bwapi.Order;
import bwapi.Unit;
import bwapi.WeaponType;
import bot.event.Event;

public class AtacarVisibles implements GenericAction, UnitDestroyObserver {

	protected final Unit unit;

	private Com com;
	private long frameEnd;

	private boolean firstExec;

	public AtacarVisibles(Com com, Unit atacante) {
		this.unit = atacante;
		this.com = com;

		this.firstExec = true;
	}

	@Override
	public void checkAndActuate() {
		if (firstExec) {
			this.frameEnd = com.bot.frames + bot.Const.FRAMES_ATTACK;
			this.firstExec = false;
		}

		if (com.bot.frames >= frameEnd) {
			System.out.println("fin ataque");
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
		unRegisterUnitObserver();
		unRegisterUnitDestroy();

		if (correct) {
			com.ComData.lastActionOk = true;
			com.ComData.unit.removeAction();
			//com.Sync.signalActionEnded();
		} else {
			// System.out.println("Miss");
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

	@Override
	public void onUnit(Unit unit) {
		if (this.unit.equals(unit))
			checkAndActuate();
	}

	@Override
	public void onUnitDestroy(Unit unit) {
		if (unit.exists() && this.unit.equals(unit)) {
			com.bot.addEvent(new Event(Event.CODE_KILLED));
		} else {
			com.bot.addEvent(new Event(Event.CODE_KILL));
		}
		onEndAction(true);
	}

	@Override
	public void registerUnitObserver() {
		this.com.bot.registerOnUnitObserver(this);
		this.com.bot.registerOnUnitDestroyObserver(this);
	}

	@Override
	public void unRegisterUnitObserver() {
		this.com.bot.unRegisterOnUnitObserver(this);
	}

	@Override
	public void unRegisterUnitDestroy() {
		this.com.bot.unRegisterOnUnitDestroyObserver(this);
	}

	@Override
	public Unit getUnit() {
		return this.unit;
	}
}
