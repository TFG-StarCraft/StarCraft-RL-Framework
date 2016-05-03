package newAgent;

import java.util.ArrayList;

import newAgent.state.State;

public interface AbstractEnvironment {
	State getInitState();
	int getNumDims();
	ArrayList<Integer> getNumValuesPerDims();
}
