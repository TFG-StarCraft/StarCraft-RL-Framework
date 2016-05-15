package newAgent.decisionMaker;

import java.util.Random;

import com.Com;

import bot.Bot;
import newAgent.Action;
import newAgent.agent.GenericAgent;
import newAgent.state.State;

public class DM_LambdaQE extends GenericDecisionMaker {

	protected static Com com;
	protected static Bot bot;
	protected GenericAgent agent;

	protected int numRandomMoves;
	
	protected Shared_LambdaQE shared;

	public DM_LambdaQE(GenericAgent agent, Shared_LambdaQE shared) {
		com = agent.getCom();
		bot = agent.getBot();
		this.agent = agent;

		this.shared = shared;
	}

	@Override
	public void run() {
		com.onSendMessage(this.getClass().getName() + " started");

		State S = agent.getInitState();
		
		Action A = nextAction(S);
		this.numRandomMoves = 0;
		// QE reset done in master

		Double R = 0.0;
		
		while (!S.isFinalState() && !isHalted()) {
			Action AA, AStar;
			double delta;

			// Blocks until action A ends
			State SS = S.executeAction(A);
			if (isHalted()) 
				return;

			R = SS.getReward();
			com.onDebugMessage(R.toString(), utils.DebugEnum.REWARD);

			AA = nextAction(SS);
			AStar = nextOptimalAction(SS);

			delta = R + shared.getGamma() * shared.getQE().getQ(SS, AStar) - shared.getQE().getQ(S, A);

			shared.updateQE(agent, S, A, AA, AStar, delta);
						
			S = SS;
			A = AA;
		}
		if (isHalted()) 
			return;
		// Params update done in master		
		agent.onEndIteration(numRandomMoves, shared.get_i(), shared.getAlpha(), shared.getEpsilon(), R);
	}
	
	private boolean halt = false;
	
	public synchronized boolean isHalted() {
		return halt;
	}

	@Override
	public synchronized void timeOut() {
		this.halt = true;
	}
	
	public Action nextOptimalAction(State S) {
		double q = Double.NEGATIVE_INFINITY;
		double qq;
		Action mov = null;
		// Probar movimientos
		for (int i = 0; i < Action.values().length; i++) {
			Action A = new Action(i, agent);
			// Hasta encontrar uno valido, y ademas sela el mejor (greedy)
			if ((qq = shared.getQE().getQ(S, A)) >= q && S.esAccionValida(A)) {
				q = qq;
				mov = A;
			}
		}

		return mov;
	}

	public Action nextAction(State S) {
		Random r = new Random();
		double e = r.nextDouble();
		Action mov = null;

		if (e < shared.getEpsilon()) {
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
