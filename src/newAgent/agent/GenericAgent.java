package newAgent.agent;

import java.util.ArrayList;

import com.Com;

import bot.Bot;
import bot.action.ActionDispatchQueue;
import bot.action.GenericAction;
import bwapi.Unit;
import newAgent.AbstractEnvironment;
import newAgent.Action;
import newAgent.decisionMaker.GenericDecisionMaker;
import newAgent.event.AbstractEvent;
import newAgent.event.factories.AbstractEventsFactory;
import newAgent.master.GenericMaster;
import utils.DebugEnum;
import utils.SafeNotify;

public abstract class GenericAgent implements AbstractEnvironment, Runnable {

	protected Com com;
	protected Bot bot;
	protected GenericMaster master;
	protected SafeNotify safeNotify;

	protected GenericDecisionMaker decisionMaker;
	protected GenericAction currentAction;

	protected AbstractEventsFactory factory;
	protected ActionDispatchQueue actionsToDispatch;
	protected ArrayList<AbstractEvent> events;

	public GenericAgent(GenericMaster master, Com com, Bot bot) {
		this.master = master;
		this.com = com;
		this.bot = bot;
		this.safeNotify = new SafeNotify();
		this.currentAction = null;

		this.events = new ArrayList<>();
		this.actionsToDispatch = new ActionDispatchQueue(com);
		setUpFactory();
	}

	///////////////////////////////////////////////////////////////////////////
	// ABSTRACT ///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	protected abstract void setUpFactory();
	// BWAPI
	public abstract void onFirstFrame();
	public abstract void onFrame();	
	public abstract void onUnitDestroy(Unit u);	
	public abstract void onFinish();
	// Actions
	protected abstract void onNewAction();
	public abstract void onEndAction(GenericAction genericAction, boolean correct);
	// Events / reward
	public abstract boolean solveEventsAndCheckEnd();
	public abstract double getRewardUpdated();
	public abstract Boolean getOnFinalUpdated();

	///////////////////////////////////////////////////////////////////////////
	// ACTIONS ////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	public void onNewAction(GenericAction action) {
		this.currentAction = action;
		this.onNewAction();
	}
	
	public void clearActionQueue() {
		this.actionsToDispatch.clear();
	}

	public void enqueueAction(Action action) {
		this.actionsToDispatch.enqueueAction(action);
	}

	public void onEndIteration(int numRandomMoves, int i, double alpha, double epsilon, Double r) {
		com.onEndIteration(0, numRandomMoves, i, alpha, epsilon, r);
	}

	public void onTimeOut() {
		decisionMaker.timeOut();
	}
	
	///////////////////////////////////////////////////////////////////////////
	// EVENTS /////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	protected void addEvent(AbstractEvent event) {
		com.onDebugMessage("EVENT from" + this.getClass().getName() + ", " + this.toString() + " at " + bot.frames,
				DebugEnum.EVENT_AT_FRAME);
		this.events.add(event);
	}
	
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
//			e.printStackTrace();
//			com.onError(e.getLocalizedMessage(), true);
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	public boolean shouldUpdateQE() {
		return master.shouldUpdateQE(this);
	}
	
	
	public Com getCom() {
		return com;
	}

	public Bot getBot() {
		return bot;
	}
	
	@Override
	public void run() {
		this.decisionMaker.run();
	}

}
