package newAgent.decisionMaker;

import newAgent.AbstractEnvironment;
import newAgent.Action;
import newAgent.agent.GenericAgent;
import newAgent.qFunction.AbstractQEFunction;
import newAgent.qFunction.QEMap;
import newAgent.state.State;

public class Shared_LambdaQE {
	// Shared
	private boolean initialized = false;
	
	private AbstractQEFunction QE;

	private double epsilon;
	private double alpha;
	private double gamma;
	private double lambda;
   
	private double init_epsilon;
	private double init_alpha;
	        
	private int i;
		
	public Shared_LambdaQE(DecisionMakerPrams params, AbstractEnvironment abstractEnvironment) {
		if (!initialized) {
			initialized = true;
			alpha = init_alpha = params.alpha;
			gamma = params.gamma;
			epsilon = init_epsilon = params.epsilon;
			lambda = params.lambda;
			
			if (QE == null)
				QE = new QEMap(abstractEnvironment);
			
			i = 0;
		}
	}
	
	protected synchronized void updateQE(GenericAgent a, State S, Action A, Action AA, Action AStar, double delta) {
		if (a.shouldUpdateQE()) {
			double E;
			
			 E = QE.getE(S, A) + 1;
			/* E update alternatives: */
			// E = 1;
			// E = (1 - alpha) * Q.getE(S, A) + 1;
			
			QE.setE(S, A, E);
			QE.replaceValues((k, v) -> {
				v.q = v.q + alpha * delta * v.e;
				if (AA.equals(AStar)) {
					v.e = gamma * lambda * v.e;
				} else {
					v.e = 0;
				}
	
				return v;
			});
		}
	}
	
	public void updateParams() {
		// Decrement alpha
		alpha = init_alpha - init_alpha * (Math.exp(- (350 / (double) (i+1))));
		// Increment epsilon
		epsilon = init_epsilon + (1 - init_epsilon) * (Math.exp(- (450 / (double) (i+1))));
		
		i++;
	}
	

	public synchronized AbstractQEFunction getQE() {
		return QE;
	}

	public double getEpsilon() {
		return epsilon;
	}

	public double getAlpha() {
		return alpha;
	}

	public double getGamma() {
		return gamma;
	}

	public double getLambda() {
		return lambda;
	}

	public int get_i() {
		return i;
	}

}
