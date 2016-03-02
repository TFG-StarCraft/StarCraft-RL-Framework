package qLearning.enviroment;

import java.util.ArrayList;

import qLearning.agent.state.State;

public interface AbstractEnviroment {
	State getInitState();
	int getNumDims();
	ArrayList<Integer> getNumValuesPerDims();
}
