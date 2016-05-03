package newAgent.decisionMaker;

import java.util.Random;

import com.Com;

import bot.Bot;
import newAgent.GenericAgent;
import newAgent.state.State;
import qLearning.agent.Action;
import qLearning.agent.qFunction.AbstractQEFunction;
import qLearning.agent.qFunction.QEMap;

public class DM_LambdaQE extends GenericDecisionMaker {

	protected Com com;
	protected Bot bot;
	protected GenericAgent agent;

	protected int numRandomMoves;

	protected static AbstractQEFunction QE;

	protected double epsilon;
	protected double alpha;
	protected double gamma;
	protected double lambda;

	protected double init_epsilon;
	protected double init_alpha;
	protected double init_gamma;
	
	public static class Params extends DecisionMakerPrams {
		public double epsilon;
		public double alpha;
		public double gamma;
		public double lambda;
	}

	public DM_LambdaQE(GenericAgent agent, Params params) {
		this.com = agent.getCom();
		this.bot = agent.getBot();
		this.agent = agent;

		if (QE == null)
			QE = new QEMap(agent);
		
		this.alpha = this.init_alpha = params.alpha;
		this.gamma = this.init_gamma = params.gamma;
		this.epsilon = this.init_epsilon = params.epsilon;
		this.lambda = params.lambda;
	}

	@Override
	public void run() {
		com.onSendMessage(this.getClass().getName() + " started");

		int i = 0;
		// TODO decisionMakerEnd
		//while (true) {
			// TODO d sync
			// com.Sync.waitForBotGameIsStarted();
			agent.waitForBotGameIsStarted();

			State S = agent.getInitState();

			Action A = nextAction(S);
			this.numRandomMoves = 0;
			QE.resetE();

			Double R = 0.0;
			
			while (!S.isFinalState()) {
				Action AA, AStar;
				double delta;

				double E;

				// Blocks until action A ends
				State SS = S.executeAction(A);

				R = SS.getReward();
				com.onDebugMessage(R.toString(), utils.DebugEnum.REWARD);

				AA = nextAction(SS);
				AStar = nextOptimalAction(SS);

				delta = R + gamma * QE.getQ(SS, AStar) - QE.getQ(S, A);

				// TODO update QE
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
				
				S = SS;
				A = AA;
			}

			// Decrement alpha
			this.alpha = this.init_alpha - this.init_alpha * (Math.exp(- (350 / (double) (i+1))));
			// Increment epsilon
			this.epsilon = this.init_epsilon + (1 - this.init_epsilon) * (Math.exp(- (450 / (double) (i+1))));
			
			// TODO decisionMakerEndIteraction
			// Iteration end
			//com.onEndIteration(steps, numRandomMoves, i, alpha, epsilon, R);

			//com.onFullQUpdate(QE.showQ());

			//com.restart();
			
			agent.onEndIteration(numRandomMoves, i, alpha, epsilon, R);
			i++;
		//}
	}

	public Action nextOptimalAction(State S) {
		double q = Double.NEGATIVE_INFINITY;
		Action mov = null;
		// Probar movimientos
		for (int i = 0; i < Action.values().length; i++) {
			Action A = new Action(i, agent);
			// Hasta encontrar uno valido, y ademas sela el mejor (greedy)
			if (QE.getQ(S, A) >= q && S.esAccionValida(A)) {
				q = QE.getQ(S, A);
				mov = A;
			}
		}

		return mov;
	}

	public Action nextAction(State S) {
		Random r = new Random();
		double e = r.nextDouble();
		Action mov = null;

		if (e < epsilon) {
			mov = nextOptimalAction(S);
		} else {
			numRandomMoves++;
			// Tomar un movimiento aleatorio valido
			while (mov == null) {
				Action A = new Action(r.nextInt(Action.values().length), agent);
				if (S.esAccionValida(A)) {
					mov = A;
				}
			}
		}

		return mov;
	}

}
