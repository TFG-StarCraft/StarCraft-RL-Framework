package qLearning.agent.algorithm;

import com.Com;

import qLearning.agent.Action;
import qLearning.agent.state.State;
import qLearning.enviroment.AbstractEnviroment;

public abstract class AbstractAlgorithm implements Runnable {
	public abstract void run();
	public abstract Action nextAction(State S, int epoch);
	    
	protected AbstractEnviroment enviroment;
	protected Com com;
	protected int numRandomMoves;
    
	protected double epsilon;
	protected double alpha;
	protected double gamma;
	
}
