package newAgent;

import java.util.ArrayList;

import com.Com;

import bwapi.Unit;
import bwapi.UnitType;
import newAgent.decisionMaker.DecisionMakerPrams;
import newAgent.decisionMaker.Shared_LambdaQE;
import newAgent.state.DataMarine;
import newAgent.state.State;
import newAgent.unit.MarineUnit;
import utils.DebugEnum;

public class Master {

	protected ArrayList<GenericAgent> agentsNotFinished;
	protected ArrayList<GenericAgent> agentsFinished;
	protected ArrayList<GenericAgent> allAgents;

	private ArrayList<Thread> threads;

	protected Com com;

	protected DecisionMakerPrams params;

	private int numAgents;
	private int learningAgent;

	public Master(Com com, DecisionMakerPrams params) {
		this.agentsNotFinished = new ArrayList<>();
		this.agentsFinished = new ArrayList<>();
		this.allAgents = new ArrayList<>();
		this.threads = new ArrayList<>();
		this.params = params;
		this.com = com;

		this.numAgents = -1;
		this.learningAgent = 0;
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
				com.bot.unRegisterUnitKilledObserver(a);
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

	public void onFrame() {
		for (GenericAgent genericAgent : agentsNotFinished) {
			genericAgent.onFrame();
		}
	}

	public void clearActionQueue() {
		for (GenericAgent genericAgent : agentsNotFinished) {
			genericAgent.clearActionQueue();
		}
	}

	public void onStart() {
		clearActionQueue();
		// TODO resetFinal ¿done?
		allAgents.clear();
		agentsNotFinished.clear();
		agentsFinished.clear();

		// Wait for threads
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
				com.onError(e.getLocalizedMessage(), true);
			}
		}

		if (shared != null)
			shared.updateParams();
		
		threads.clear();
	}

	Shared_LambdaQE shared;

	public void onFirstFrame() {
		// TODO d first frame master

		if (shared == null)
			shared = new Shared_LambdaQE(params, new AbstractEnvironment() {

				@Override
				public int getNumDims() {
					return DataMarine.getNumDims();
				}

				@Override
				public ArrayList<Integer> getNumValuesPerDims() {
					return DataMarine.getNumValuesPerDims();
				}

				@Override
				public State getInitState() {
					// TODO
					throw new RuntimeException();
				}
			});

		for (Unit unit : com.bot.self.getUnits()) {
			if (unit.getType() == UnitType.Terran_Marine) {
				GenericAgent a = new MarineUnit(this, unit, com, com.bot, shared);
				this.agentsNotFinished.add(a);
				this.allAgents.add(a);
			}
		}

		if (numAgents == -1) {
			numAgents = allAgents.size();
			learningAgent = 0;
		} else if (numAgents != allAgents.size()) {
			com.onError("Previous num agents differs from current", true);
		}

		learningAgent = (learningAgent + 1) % numAgents;

		com.onDebugMessage("Agent num" + learningAgent + ", " + allAgents.get(learningAgent).getClass() + ", learning",
				DebugEnum.AGENT_LEARNING);
		
		shared.getQE().resetE();

		for (GenericAgent genericAgent : agentsNotFinished) {
			com.bot.registerUnitKilledObserver(genericAgent);
			Thread t = new Thread(genericAgent);
			t.start();
			threads.add(t);
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// SYNC ///////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	// public void signalGameIsReady() {
	// for (GenericAgent genericAgent : agentsNotFinished) {
	// genericAgent.signalGameIsReady();
	// }
	// }

	public void signalInitIsDone() {
		for (GenericAgent genericAgent : agentsNotFinished) {
			genericAgent.signalInitIsDone();
		}
	}

	public boolean shouldUpdateQE(GenericAgent genericAgent) {
		return genericAgent == allAgents.get(learningAgent);
	}

}
