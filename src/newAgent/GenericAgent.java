package newAgent;

import java.util.ArrayList;

import com.Com;

import bot.Bot;
import bot.action.ActionDispatchQueue;
import bot.action.GenericAction;
import bot.observers.OnUnitObserver;
import bot.observers.UnitKilledObserver;
import bwapi.Unit;
import newAgent.decisionMaker.GenericDecisionMaker;
import newAgent.event.AbstractEvent;
import newAgent.event.factories.AbstractEventsFactory;
import newAgent.state.State;
import qLearning.agent.Action;
import qLearning.environment.AbstractEnvironment;
import utils.DebugEnum;

public abstract class GenericAgent implements OnUnitObserver, UnitKilledObserver, AbstractEnvironment {

	protected Com com;
	protected Bot bot;
	protected Master master;

	protected GenericDecisionMaker decisionMaker;
	protected Unit unit;
	protected GenericAction currentAction;

	protected AbstractEventsFactory factory;
	protected ActionDispatchQueue actionsToDispatch;
	protected ArrayList<AbstractEvent> events;

	public GenericAgent(Unit unit, Com com, Bot bot) {
		this.unit = unit;
		this.com = com;
		this.bot = bot;
		this.currentAction = null;

		this.events = new ArrayList<>();
		this.actionsToDispatch = new ActionDispatchQueue(com);
		setUpFactory();
	}

	protected abstract void setUpFactory();
	
	public void clearActionQueue() {
		this.actionsToDispatch.clear();
	}
	
	public void enqueueAction(Action action) {
		this.actionsToDispatch.enqueueAction(action);
	}
	

	public void onEndIteration(int numRandomMoves, int i, double alpha, double epsilon, Double r) {
		// TODO Auto-generated method stub
		
	}
	

	public void onNewAction(GenericAction action) {
		this.currentAction = action;
		this.onNewAction();
	}
	
	public abstract void notifyEnd(boolean tmp);

	public abstract boolean solveEventsAndCheckEnd();
	
	public abstract void onFrame();
	
	public abstract void onNewAction();

	public abstract void onEndAction(GenericAction genericAction, boolean correct);

	public abstract double getReward(State state);

	////////////////////
	// onUnitObserver //
	////////////////////

	@Override
	public abstract void onUnit(Unit unit);

	@Override
	public Unit getUnitObserved() {
		// TODO Auto-generated method stub
		return unit;
	}

	@Override
	public void registerOnUnitObserver() {
		// TODO Auto-generated method stub

	}

	@Override
	public void unRegisterOnUnitObserver() {
		// TODO Auto-generated method stub

	}

	////////////////////////
	// unitKilledObserver //
	////////////////////////

	@Override
	public abstract void onUnitKilled(Unit unit);

	@Override
	public void registerUnitKilledObserver() {
		// TODO Auto-generated method stub

	}

	@Override
	public void unRegisterUnitKilledObserver() {
		// TODO Auto-generated method stub

	}

	////////////
	// Events //
	////////////

	protected void addEvent(AbstractEvent event) {
		com.onDebugMessage("EVENT from" + this.getClass().getName() + ", " + this.toString() + " at " + bot.frames,
				DebugEnum.EVENT_AT_FRAME);
		this.events.add(event);
	}

	public Com getCom() {
		return com;
	}

	public Bot getBot() {
		return bot;
	}

	public abstract Boolean getOnFinalUpdated();


}
