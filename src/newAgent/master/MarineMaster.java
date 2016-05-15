package newAgent.master;

import java.util.ArrayList;

import com.Com;

import bwapi.Unit;
import bwapi.UnitType;
import newAgent.AbstractEnvironment;
import newAgent.agent.GenericAgent;
import newAgent.agent.unit.MarineUnit;
import newAgent.decisionMaker.DecisionMakerPrams;
import newAgent.decisionMaker.Shared_LambdaQE;
import newAgent.state.DataMarine;
import newAgent.state.State;
import utils.DebugEnum;

public class MarineMaster extends GenericMaster {

	public MarineMaster(Com com, DecisionMakerPrams params) {
		super(com, params);
	}

	@Override
	public void onFirstFrame() {
		halted = false;
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

		if (numAgents == -1) { // First execution
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
			genericAgent.onFirstFrame();
			Thread t = new Thread(genericAgent);
			t.start();
			threads.add(t);
		}
	}	


}
