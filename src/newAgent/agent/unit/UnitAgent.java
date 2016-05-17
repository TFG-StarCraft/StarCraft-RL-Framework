package newAgent.agent.unit;

import com.Com;

import bot.Bot;
import bwapi.Unit;
import newAgent.agent.GenericAgent;
import newAgent.master.GenericMaster;

public abstract class UnitAgent extends GenericAgent {
		
	public UnitAgent(GenericMaster master, Unit unit, Com com, Bot bot) {
		super(master, com, bot);
		this.unit = unit;
	}

	@Override
	public void onFirstFrame() {
		//registerUnitKilledObserver();
	}
	
	@Override
	public void onFinish() {
		//unRegisterUnitKilledObserver();
	}
	
	protected Unit unit;

	////////////////////
	// onUnitObserver //
	////////////////////

	public void onUnit(Unit unit) {
		currentAction.onUnit(unit);
	}
	
	public Unit getUnitObserved() {
		return unit;
	}

	////////////////////////
	// unitKilledObserver //
	////////////////////////

	public abstract void onUnitDestroy(Unit unit);

}
