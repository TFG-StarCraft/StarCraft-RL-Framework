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
import utils.DebugEnum;
import utils.SafeNotify;

public abstract class GenericAgent implements OnUnitObserver, UnitKilledObserver, AbstractEnvironment, Runnable {

	protected Com com;
	protected Bot bot;
	protected Master master;
	protected SafeNotify safeNotify;

	protected GenericDecisionMaker decisionMaker;
	protected Unit unit;
	protected GenericAction currentAction;

	protected AbstractEventsFactory factory;
	protected ActionDispatchQueue actionsToDispatch;
	protected ArrayList<AbstractEvent> events;

	public GenericAgent(Master master, Unit unit, Com com, Bot bot) {
		this.master = master;
		this.unit = unit;
		this.com = com;
		this.bot = bot;
		this.safeNotify = new SafeNotify();
		this.currentAction = null;

		this.events = new ArrayList<>();
		this.actionsToDispatch = new ActionDispatchQueue(com);
		setUpFactory();
	}

	@Override
	public void run() {
		this.decisionMaker.run();
	}
	
	protected abstract void setUpFactory();

	public void clearActionQueue() {
		this.actionsToDispatch.clear();
	}

	public void enqueueAction(Action action) {
		this.actionsToDispatch.enqueueAction(action);
	}

	public void onEndIteration(int numRandomMoves, int i, double alpha, double epsilon, Double r) {
		com.onEndIteration(0, numRandomMoves, i, alpha, epsilon, r);
	}

	public void onNewAction(GenericAction action) {
		this.currentAction = action;
		this.onNewAction();
	}

	public abstract boolean solveEventsAndCheckEnd();

	public abstract void onFrame();

	public abstract void onNewAction();

	public abstract void onEndAction(GenericAction genericAction, boolean correct);

	public abstract double getRewardUpdated();

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

	///////////////////////////////////////////////////////////////////////////
	// SYNC ///////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	public void signalInitIsDone() {
		this.safeNotify.safeNotify();
	}

	public void waitForBotEndsInit() {
		try {
			this.safeNotify.safeWait();
		} catch (InterruptedException e) {
			e.printStackTrace();
			com.onError(e.getLocalizedMessage(), true);
		}
	}

	public void signalActionEnded() {
		this.safeNotify.safeNotify();

	}

	public void waitForActionEnds() {
		try {
			this.safeNotify.safeWait();
		} catch (InterruptedException e) {
			e.printStackTrace();
			com.onError(e.getLocalizedMessage(), true);
		}
	}

	public boolean shouldUpdateQE() {
		return master.shouldUpdateQE(this);
	}

}
