package newAgent.master;

import java.util.ArrayList;

import com.Com;

import bwapi.Unit;
import newAgent.agent.GenericAgent;
import newAgent.decisionMaker.DecisionMakerPrams;
import newAgent.decisionMaker.Shared_LambdaQE;
import utils.DebugEnum;

public abstract class GenericMaster {

	protected ArrayList<GenericAgent> agentsNotFinished;
	protected ArrayList<GenericAgent> agentsFinished;
	protected ArrayList<GenericAgent> allAgents;

	protected ArrayList<Thread> threads;

	protected Com com;

	protected DecisionMakerPrams params;
	protected Shared_LambdaQE shared;

	protected int numAgents;
	protected int learningAgent;
	protected boolean halted;
	
	public GenericMaster(Com com, DecisionMakerPrams params) {
		this.agentsNotFinished = new ArrayList<>();
		this.agentsFinished = new ArrayList<>();
		this.allAgents = new ArrayList<>();
		this.threads = new ArrayList<>();
		this.params = params;
		this.com = com;

		this.numAgents = -1;
		this.learningAgent = 0;
	}

	public void onStart() {
		clearActionQueue();
		allAgents.clear();
		agentsNotFinished.clear();
		agentsFinished.clear();

		// Wait for threads
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
//				e.printStackTrace();
//				com.onError(e.getLocalizedMessage(), true);
			}
		}

		if (shared != null)
			shared.updateParams();
		
		threads.clear();
	}

	public abstract void onFirstFrame();
	
	public void onUnitDestroy(Unit u) {
		for (GenericAgent genericAgent : agentsNotFinished) {
			genericAgent.onUnitDestroy(u);
		}
	}

	public void onFrame() {
		for (GenericAgent genericAgent : agentsNotFinished) {
			genericAgent.onFrame();
		}
	}

	public boolean solveEventsAndCheckEnd() {
		boolean endConditionSatisfied = false;
		int i = 0;

		while (i < agentsNotFinished.size()) {
			// if is final
			GenericAgent a;
			if (agentsNotFinished.get(i).solveEventsAndCheckEnd()) {
				a = agentsNotFinished.remove(i);
				agentsFinished.add(a);
				a.onFinish();
			} else {
				i++;
			}
		}

		if (agentsNotFinished.size() == 0) {
			endConditionSatisfied = true;
			com.bot.requestRestart();
		}

		return endConditionSatisfied;
	}

	public synchronized void onTimeOut() {
		if (!halted) {
			halted = true;
			for (GenericAgent genericAgent : agentsNotFinished) {
				genericAgent.onTimeOut();
			}
			for (Thread thread : threads) {
				thread.interrupt();
			}
			com.onDebugMessage("Time out", DebugEnum.TIME_OUT);
			com.bot.requestRestart();
		}
	}

	public void clearActionQueue() {
		for (GenericAgent genericAgent : agentsNotFinished) {
			genericAgent.clearActionQueue();
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// SYNC ///////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	public void signalInitIsDone() {
		for (GenericAgent genericAgent : agentsNotFinished) {
			genericAgent.signalInitIsDone();
		}
	}

	public boolean shouldUpdateQE(GenericAgent genericAgent) {
		return !halted && genericAgent == allAgents.get(learningAgent);
	}

}
