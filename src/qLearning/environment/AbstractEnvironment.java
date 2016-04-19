package qLearning.environment;

import java.util.ArrayList;

import qLearning.agent.state.State;

public interface AbstractEnvironment {
	State getInitState();
	int getNumDims();
	ArrayList<Integer> getNumValuesPerDims();
}
