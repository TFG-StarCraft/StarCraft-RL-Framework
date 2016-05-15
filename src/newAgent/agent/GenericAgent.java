package newAgent.agent;

import java.util.ArrayList;

import com.Com;

import bot.Bot;
import bot.action.ActionDispatchQueue;
import bot.action.GenericAction;
import newAgent.AbstractEnvironment;
import newAgent.Action;
import newAgent.Master;
import newAgent.decisionMaker.GenericDecisionMaker;
import newAgent.event.AbstractEvent;
import newAgent.event.factories.AbstractEventsFactory;
import utils.DebugEnum;
import utils.SafeNotify;

public abstract class GenericAgent implements AbstractEnvironment, Runnable {

	protected Com com;
	protected Bot bot;
	protected Master master;
	protected SafeNotify safeNotify;

	protected GenericDecisionMaker decisionMaker;
	protected GenericAction currentAction;

	protected AbstractEventsFactory factory;
	protected ActionDispatchQueue actionsToDispatch;
	protected ArrayList<AbstractEvent> events;

	public GenericAgent(Master master, Com com, Bot bot) {
		this.master = master;
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

	public abstract void onFirstFrame();
	public abstract void onFinish();
	
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

	public void onTimeOut() {
		decisionMaker.timeOut();
	}
	
	public abstract void onNewAction();

	public abstract void onEndAction(GenericAction genericAction, boolean correct);

	public abstract double getRewardUpdated();

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
//			e.printStackTrace();
//			com.onError(e.getLocalizedMessage(), true);
		}
	}

	public boolean shouldUpdateQE() {
		return master.shouldUpdateQE(this);
	}
}
