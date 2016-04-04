package qLearning.agent.algorithm;

import com.Com;

import qLearning.agent.Action;
import qLearning.agent.state.State;
import qLearning.enviroment.AbstractEnviroment;

public abstract class AbstractAlgorithm implements Runnable {
	public abstract void run();
	public abstract Action nextAction(State S);
	    
	protected AbstractEnviroment enviroment;
	protected Com com;
	protected int numRandomMoves;
    
	protected double epsilon;
	protected double alpha;
	protected double gamma;
	
	protected double init_epsilon;
	protected double init_alpha;
	protected double init_gamma;
	
	public AbstractAlgorithm(Com com, AbstractEnviroment e, double alpha, double gamma, double epsilon) {
		this.com = com;
		this.enviroment = e;
		
		this.alpha = this.init_alpha = alpha;
		this.gamma = this.init_gamma = gamma;
		this.epsilon = this.init_epsilon = epsilon;
	}	
}
