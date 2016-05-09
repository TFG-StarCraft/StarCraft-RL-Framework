package newAgent;

import java.util.ArrayList;

import com.Com;

import bwapi.Unit;
import bwapi.UnitType;
import newAgent.decisionMaker.DecisionMakerPrams;
import newAgent.unit.MarineUnit;

public class Master {

	protected ArrayList<GenericAgent> agentsNotFinished;
	protected ArrayList<GenericAgent> agentsFinished;

	private ArrayList<Thread> threads;
	
	protected Com com;
	
	protected DecisionMakerPrams params;
	
	public Master(Com com, DecisionMakerPrams params) {
		this.agentsNotFinished = new ArrayList<>();
		this.agentsFinished = new ArrayList<>();
		this.threads = new ArrayList<>();
		this.params = params;
		this.com = com;
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
		
		threads.clear();
	}
		
	public void onFirstFrame() {
		// TODO d first frame master

		for (Unit unit : com.bot.self.getUnits()) {
			if (unit.getType() == UnitType.Terran_Marine) {
				this.agentsNotFinished.add(new MarineUnit(unit, com, com.bot, params));
			}
		}
		
		for (GenericAgent genericAgent : agentsNotFinished) {
			com.bot.registerUnitKilledObserver(genericAgent);
			Thread	t = new Thread(genericAgent);
			t.start();
			threads.add(t);
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	// SYNC ///////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

//	public void signalGameIsReady() {
//		for (GenericAgent genericAgent : agentsNotFinished) {
//			genericAgent.signalGameIsReady();
//		}
//	}

	public void signalInitIsDone() {
		for (GenericAgent genericAgent : agentsNotFinished) {
			genericAgent.signalInitIsDone();
		}
	}

}
