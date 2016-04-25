package qLearning.agent.algorithm;

import com.Com;

import newAgent.state.State;
import qLearning.agent.Action;
import qLearning.environment.AbstractEnvironment;

public abstract class AbstractAlgorithm implements Runnable {
	public abstract void run();
	public abstract Action nextAction(State S);
	    
	protected AbstractEnvironment environment;
	protected Com com;
	protected int numRandomMoves;
    
	protected double epsilon;
	protected double alpha;
	protected double gamma;
	
	protected double init_epsilon;
	protected double init_alpha;
	protected double init_gamma;
	
	public AbstractAlgorithm(Com com, AbstractEnvironment e, double alpha, double gamma, double epsilon) {
		this.com = com;
		this.environment = e;
		
		this.alpha = this.init_alpha = alpha;
		this.gamma = this.init_gamma = gamma;
		this.epsilon = this.init_epsilon = epsilon;
	}	
}
