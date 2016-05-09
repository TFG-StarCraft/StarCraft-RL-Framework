package newAgent.decisionMaker;

import newAgent.AbstractEnvironment;
import newAgent.Action;
import newAgent.qFunction.AbstractQEFunction;
import newAgent.qFunction.QEMap;
import newAgent.state.State;

public class Shared_LambdaQE {
	// Shared
	protected boolean initialized = false;
	
	AbstractQEFunction QE;

	double epsilon;
	double alpha;
	double gamma;
	double lambda;

	double init_epsilon;
	double init_alpha;
	double init_gamma;
	
	int i;
		
	public Shared_LambdaQE(DecisionMakerPrams params, AbstractEnvironment abstractEnvironment) {
		if (!initialized) {
			initialized = true;
			alpha = init_alpha = params.alpha;
			gamma = init_gamma = params.gamma;
			epsilon = init_epsilon = params.epsilon;
			lambda = params.lambda;
			
			if (QE == null)
				QE = new QEMap(abstractEnvironment);
			
			i = 0;
		}
	}
		
	protected synchronized void resetQE() {
		QE.resetE();
	}
	
	protected synchronized void updateQE(State S, Action A, Action AA, Action AStar, double delta) {
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
	
	protected synchronized void updateParams() {
		// Decrement alpha
		alpha = init_alpha - init_alpha * (Math.exp(- (350 / (double) (i+1))));
		// Increment epsilon
		epsilon = init_epsilon + (1 - init_epsilon) * (Math.exp(- (450 / (double) (i+1))));
		
		i++;
	}
	

	public AbstractQEFunction getQE() {
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
