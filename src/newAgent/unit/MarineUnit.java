package newAgent.unit;

import java.util.ArrayList;

import com.Com;

import bot.Bot;
import bwapi.Unit;
import newAgent.decisionMaker.DM_LambdaQE;
import newAgent.decisionMaker.GenericDecisionMaker;
import newAgent.state.DataMarine;
import newAgent.state.State;
import qLearning.Const;

public class MarineUnit extends UnitAgent {

	public MarineUnit(Unit unit, Com com, Bot bot) {
		super(unit, com, bot);
		this.decisionMaker = new DM_LambdaQE(this, );
	}

	@Override
	public State getInitState() {
		// TODO sync
		// com.Sync.waitForBotEndsInit();
		return new State(this, new DataMarine(com, unit));
	}

	@Override
	public int getNumDims() {
		return DataMarine.getNumDims();
	}

	@Override
	public ArrayList<Integer> getNumValuesPerDims() {
		return DataMarine.getNumValuesPerDims();
	}

	@Override
	public double getReward(State state) {
		if (state.isFinalState()) {
			if (com.ComData.isFinalStateGoal)
				return Const.REWARD_SUCCESS;
			else {
				return Const.REWARD_FAIL;
			}
		} else {
			// TODO (botDestruirUnidad)
		}
	}
	
}
