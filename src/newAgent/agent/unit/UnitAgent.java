package newAgent.agent.unit;

import com.Com;

import bot.Bot;
import bot.action.GenericAction;
import bwapi.Unit;
import newAgent.agent.GenericAgent;
import newAgent.master.GenericMaster;

public abstract class UnitAgent extends GenericAgent {

	protected Unit unit;
	
	public UnitAgent(GenericMaster master, Unit unit, Com com, Bot bot) {
		super(master, com, bot);
		this.unit = unit;
	}
	
	////////////////////
	// onUnitObserver //
	////////////////////

	public Unit getUnitObserved() {
		return unit;
	}
	
	///////////////////////////////////////////////////////////////////////////
	// ABSTRACT ///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	protected abstract void setUpFactory();
	// BWAPI
	@Override
	public void onFirstFrame() { }
	public abstract void onFrame();	
	public abstract void onUnitDestroy(Unit u);	
	@Override
	public void onFinish() { }
	// Actions
	protected abstract void onNewAction();
	public abstract void onEndAction(GenericAction genericAction, boolean correct);
	// Events / reward
	public abstract boolean solveEventsAndCheckEnd();
	public abstract double getRewardUpdated();
	public abstract Boolean getOnFinalUpdated();

}
