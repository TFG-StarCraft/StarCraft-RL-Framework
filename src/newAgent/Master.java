package newAgent;

import java.util.ArrayList;

import com.Com;

import bwapi.Unit;
import bwapi.UnitType;
import newAgent.unit.MarineUnit;

public class Master {

	protected ArrayList<GenericAgent> agentsNotFinished;
	protected ArrayList<GenericAgent> agentsFinished;

	private ArrayList<Thread> threads;
	
	protected Com com;
	
	public Master(Com com) {
		this.agentsNotFinished = new ArrayList<>();
		this.agentsFinished = new ArrayList<>();
		this.threads = new ArrayList<>();
		this.com = com;
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
		// TODO resetFinal ¿done?
		agentsNotFinished.clear();
		agentsFinished.clear();

		// Wait for threads
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				com.onError(e.getLocalizedMessage(), true);
			}
		}
		
		threads.clear();
	}
		
	public void onFirstFrame() {
		// TODO d first frame master

		for (Unit unit : com.bot.self.getUnits()) {
			if (unit.getType() == UnitType.Terran_Marine) {
				this.agentsNotFinished.add(new MarineUnit(unit, com, com.bot));
			}
		}
		
		for (GenericAgent genericAgent : agentsNotFinished) {
			Thread	t = new Thread(genericAgent);
			t.start();
			threads.add(t);
		}
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
