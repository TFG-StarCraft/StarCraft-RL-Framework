package qLearning.enviroment;

import qLearning.agent.State;

public interface AbstractEnviroment {

	public int getSizeX();
	public int getSizeY();

	public State getInitState();
}
