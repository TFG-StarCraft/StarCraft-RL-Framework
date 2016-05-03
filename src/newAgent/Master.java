package newAgent;

import java.util.ArrayList;

public class Master {

	protected ArrayList<GenericAgent> agentsNotFinished;
	protected ArrayList<GenericAgent> agentsFinished;

	public Master() {
		this.agentsNotFinished = new ArrayList<>();
		this.agentsFinished = new ArrayList<>();
	}
	
	public boolean solveEventsAndCheckEnd() {
		boolean endConditionSatisfied = false;
		boolean tmp;
		int i = 0;
		
		while (i < agentsNotFinished.size()) {
			// if is final
			if (tmp = agentsNotFinished.get(i).solveEventsAndCheckEnd()) {
				
				agentsNotFinished.get(i).notifyEnd(tmp);
				
				agentsFinished.add(agentsNotFinished.remove(i));
			} else {
				i++;
			}
		}
		
		if (agentsNotFinished.size() == 0)
			endConditionSatisfied = true;

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
		// TODO resetFinal
	}
	
	public void onFirstFrame() {
		// TODO first frame master
		// init agents?
	}
	
	///////////////////////////////////////////////////////////////////////////
	// SYNC ///////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	public void signalGameIsReady() {
		for (GenericAgent genericAgent : agentsNotFinished) {
			genericAgent.signalGameIsReady();
		}
	}

	public void signalInitIsDone() {
		for (GenericAgent genericAgent : agentsNotFinished) {
			genericAgent.signalInitIsDone();
		}
	}

}
