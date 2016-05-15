package newAgent.agent.unit;

import com.Com;

import bot.Bot;
import bot.observers.OnUnitObserver;
import bot.observers.UnitKilledObserver;
import bwapi.Unit;
import newAgent.agent.GenericAgent;
import newAgent.master.GenericMaster;

public abstract class UnitAgent extends GenericAgent implements OnUnitObserver, UnitKilledObserver {
		
	public UnitAgent(GenericMaster master, Unit unit, Com com, Bot bot) {
		super(master, com, bot);
		this.unit = unit;
	}

	@Override
	public void onFirstFrame() {
		registerUnitKilledObserver();
	}
	
	@Override
	public void onFinish() {
		unRegisterUnitKilledObserver();
	}
	
	protected Unit unit;

	////////////////////
	// onUnitObserver //
	////////////////////

	public void onUnit(Unit unit) {
		currentAction.onUnit(unit);
	}
	
	@Override
	public Unit getUnitObserved() {
		return unit;
	}

	@Override
	public void registerOnUnitObserver() {
	}

	@Override
	public void unRegisterOnUnitObserver() {
	}

	////////////////////////
	// unitKilledObserver //
	////////////////////////

	@Override
	public abstract void onUnitKilled(Unit unit);

	@Override
	public void registerUnitKilledObserver() {
		bot.registerUnitKilledObserver(this);
	}

	@Override
	public void unRegisterUnitKilledObserver() {
		bot.unRegisterUnitKilledObserver(this);
	}

}
