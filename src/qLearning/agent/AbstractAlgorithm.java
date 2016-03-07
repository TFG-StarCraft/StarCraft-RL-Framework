package qLearning.agent;

import com.Com;

import qLearning.agent.qFunction.AbstractQFunction;
import qLearning.agent.qFunction.QMap;
import qLearning.agent.state.State;
import qLearning.enviroment.AbstractEnviroment;

public abstract class AbstractAlgorithm implements Runnable {
	public abstract void run();
	public abstract Action nextAction(State S, int epoch);
	
	protected QMap Q;
    
	protected AbstractEnviroment enviroment;
	protected Com com;
	protected int numRandomMoves;
    
	protected double epsilon;
	protected double alpha;
	protected double gamma;

	public AbstractAlgorithm(Com com, AbstractEnviroment e, double alpha, double gamma, double epsilon) {
		this(com, e);
		this.alpha = alpha;
		this.gamma = gamma;
		this.epsilon = epsilon;
	}
	
	public AbstractAlgorithm(Com com, AbstractEnviroment e) {
		this.com = com;

		this.enviroment = e;
		
		this.Q = new QMap(e);
		
		this.alpha = qLearning.Const.ALPHA;
		this.gamma = qLearning.Const.GAMMA;
		this.epsilon = qLearning.Const.EPSLLON_EGREEDY;
	}
}
