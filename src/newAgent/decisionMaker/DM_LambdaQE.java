package newAgent.decisionMaker;

import java.util.Random;

import com.Com;

import bot.Bot;
import newAgent.Action;
import newAgent.GenericAgent;
import newAgent.qFunction.AbstractQEFunction;
import newAgent.qFunction.QEMap;
import newAgent.state.State;

public class DM_LambdaQE extends GenericDecisionMaker {

	protected static Com com;
	protected static Bot bot;
	protected GenericAgent agent;

	protected int numRandomMoves;

	// Shared
	protected static boolean initialized = false;
	
	protected static AbstractQEFunction QE;

	protected static double epsilon;
	protected static double alpha;
	protected static double gamma;
	protected static double lambda;

	protected static double init_epsilon;
	protected static double init_alpha;
	protected static double init_gamma;
	
	protected static int i;
		
	protected synchronized static void initShared(DecisionMakerPrams params, GenericAgent agent) {
		if (!initialized) {
			initialized = true;
			alpha = init_alpha = params.alpha;
			gamma = init_gamma = params.gamma;
			epsilon = init_epsilon = params.epsilon;
			lambda = params.lambda;
			
			if (QE == null)
				QE = new QEMap(agent);
			
			i = 0;
		}
	}
		
	protected synchronized static void resetQE() {
		QE.resetE();
	}
	
	protected synchronized static void updateQE(State S, Action A, Action AA, Action AStar, double delta) {
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
	
	protected synchronized static void updateParams() {
		// Decrement alpha
		alpha = init_alpha - init_alpha * (Math.exp(- (350 / (double) (i+1))));
		// Increment epsilon
		epsilon = init_epsilon + (1 - init_epsilon) * (Math.exp(- (450 / (double) (i+1))));
		
		i++;
	}

	public DM_LambdaQE(GenericAgent agent, DecisionMakerPrams params) {
		com = agent.getCom();
		bot = agent.getBot();
		this.agent = agent;

		initShared(params, agent);		
	}

	@Override
	public void run() {
		com.onSendMessage(this.getClass().getName() + " started");

		// TODO decisionMakerEnd
		// while (true) {
		// TODO d sync
		// com.Sync.waitForBotGameIsStarted();
		// agent.waitForBotGameIsStarted();

		State S = agent.getInitState();
		System.out.println("Init state done");
		Action A = nextAction(S);
		this.numRandomMoves = 0;
		resetQE();

		Double R = 0.0;
		
		while (!S.isFinalState()) {
			Action AA, AStar;
			double delta;

			// Blocks until action A ends
			State SS = S.executeAction(A);

			R = SS.getReward();
			com.onDebugMessage(R.toString(), utils.DebugEnum.REWARD);

			AA = nextAction(SS);
			AStar = nextOptimalAction(SS);

			delta = R + gamma * QE.getQ(SS, AStar) - QE.getQ(S, A);

			updateQE(S, A, AA, AStar, delta);
						
			S = SS;
			A = AA;
		}

		updateParams();
				
		// TODO decisionMakerEndIteraction
		// Iteration end
		//com.onEndIteration(steps, numRandomMoves, i, alpha, epsilon, R);

		//com.onFullQUpdate(QE.showQ());

		//com.restart();
		
		agent.onEndIteration(numRandomMoves, i, alpha, epsilon, R);
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
