package qLearning.enviroment;

import java.util.ArrayList;

import com.Com;

import qLearning.agent.state.DataRelative;
import qLearning.agent.state.State;

public class SCEnviroment implements AbstractEnviroment {

	private Com com;

	public SCEnviroment(Com com) {
		this.com = com;
	}

	@Override
	public State getInitState() {
		com.Sync.waitForBotEndsInit();

		return new State(new DataRelative(com), this, com, true);
	}

	@Override
	public int getNumDims() {
		int r = new DataRelative(com).getNumDims();
		
		return r;
	}

	@Override
	public ArrayList<Integer> getNumValuesPerDims() {
		ArrayList<Integer> r = new ArrayList<>(new DataRelative(com).getNumValuesPerDims());
		System.gc();
		
		return r;
	}

}
