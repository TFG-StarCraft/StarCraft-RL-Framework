package qLearning.agent.algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

import com.Com;

import newAgent.Const;
import newAgent.state.State;
import qLearning.agent.Action;
import qLearning.agent.qFunction.AbstractQFunction;
import qLearning.agent.qFunction.QMap;
import qLearning.environment.AbstractEnvironment;

public class OneStepQ extends AbstractAlgorithm {
	
	private AbstractQFunction Q;

	public OneStepQ(Com com, AbstractEnvironment e, double alpha, double gamma, double epsilon) {
		super(com, e, alpha, gamma, epsilon);
		this.Q = new QMap(e);
	}

	public void run() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File("results " + "e = " + epsilon + " a = " + alpha + " g = " + gamma));
		} catch (FileNotFoundException e) {
			com.onError(e.getLocalizedMessage(), false);
		}

		com.onSendMessage("q-Learning started");
		for (int i = 0; i < Const.NUM_EPISODIOS; i++) {

			com.Sync.waitForBotGameIsStarted();		
			
			State S = environment.getInitState();
			int movimientos = 0;
			this.numRandomMoves = 0;

			//com.Sync.signalAgentIsStarting();
			Double R = 0.0;
			while (!S.isFinalState()) {
				Action A = nextAction(S);

				// Blocks until action A ends
				State SS = S.executeAction(A);

				R = SS.getReward();
				com.onDebugMessage(R.toString(), utils.DebugEnum.REWARD);
				double maxq = Double.NEGATIVE_INFINITY;
				// Probar movimientos
				for (int k = 0; k < Action.values().length; k++) {
					Action a = new Action(k);
					// Hasta encontrar uno valido, y ademas sea el mejor
					// (greedy)
					if (Q.get(SS, a) >= maxq && SS.esAccionValida(a)) {
						maxq = Q.get(SS, a);
					}
				}

				Q.set(S, A, Q.get(S, A) + alpha * (R + gamma * maxq - Q.get(S, A)));
				S = SS;
				movimientos++;
			}
			// Iteration end
			com.onEndIteration(movimientos, numRandomMoves, i, alpha, epsilon, R);
			pw.println(i + "\t" + movimientos + "\t" + numRandomMoves);
			pw.flush();
			
			com.onFullQUpdate(Q.showQ());

			com.restart();
		}
		com.onEndTrain();
		pw.close();
	}

	public Action nextAction(State S) {

		Random r = new Random();
		double e = r.nextDouble();
		Action mov = null;

		if (e < epsilon) {
			double q = Double.NEGATIVE_INFINITY;

			// Probar movimientos
			for (int i = 0; i < Action.values().length; i++) {
				Action A = new Action(i);
				// Hasta encontrar uno valido, y ademas sela el mejor (greedy)
				if (Q.get(S, A) >= q && S.esAccionValida(A)) {
					q = Q.get(S, A);
					mov = new Action(i);
				}
			}
		} else {
			numRandomMoves++;
			// Tomar un movimiento aleatorio valido
			while (mov == null) {
				Action A = new Action(r.nextInt(Action.values().length));
				if (S.esAccionValida(A)) {
					mov = A;
				}
			}
		}
		
		return mov;
	}
	
}
