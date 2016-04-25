package qLearning.environment;

import java.util.ArrayList;

import com.Com;

import newAgent.state.DataMarine;
import newAgent.state.State;
import newAgent.state.StateData;

public class SCEnvironment implements AbstractEnvironment {

	private Com com;

	public SCEnvironment(Com com) {
		this.com = com;
	}

	@Override
	public State getInitState() {
		com.Sync.waitForBotEndsInit();

		return new State(com);
	}

	@Override
	public int getNumDims() {
		int r = DataMarine.getNumDims();
		
		return r;
	}

	@Override
	public ArrayList<Integer> getNumValuesPerDims() {
		return DataMarine.getNumValuesPerDims();
	}

}
