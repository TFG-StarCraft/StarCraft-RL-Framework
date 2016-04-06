package qLearning.agent.algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

import com.Com;

import qLearning.Const;
import qLearning.agent.Action;
import qLearning.agent.qFunction.AbstractQEFunction;
import qLearning.agent.qFunction.QEMap;
import qLearning.agent.state.State;
import qLearning.enviroment.AbstractEnviroment;

public class LambdaQ extends AbstractAlgorithm {

	private double lambda;
	private double init_lambda;

	private AbstractQEFunction QE;

	public LambdaQ(Com com, AbstractEnviroment e, double alpha, double gamma, double epsilon, double lambda) {
		super(com, e, alpha, gamma, epsilon);
		this.lambda = this.init_lambda = lambda;
	}

	public void run() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(
					new File("results " + "e = " + epsilon + " a = " + alpha + " g = " + gamma + " l = " + lambda));
		} catch (FileNotFoundException e) {
			com.onError(e.getLocalizedMessage(), false);
		}

		try {
			com.onSendMessage("q-Learning started");
			for (int i = 0; i < Const.NUM_EPISODIOS; i++) {
				com.Sync.waitForBotGameIsStarted();
				
				State S = enviroment.getInitState();

				// One time init QE_MAP, done here because it needs the enviroment initialized
				if (this.QE == null)
					this.QE = new QEMap(enviroment);
				
				Action A = nextAction(S);
				int steps = 0;
				this.numRandomMoves = 0;
				this.QE.resetE();

				// com.Sync.signalAgentIsStarting();

				while (!S.isFinalEnd()) {
					// Blocks until action A ends
					Action AA, AStar;
					double delta;

					double E;

					State SS = S.executeAction(A);

					Double R = SS.getReward();
					com.onDebugMessage(R.toString(), utils.DebugEnum.REWARD);

					AA = nextAction(SS);
					AStar = nextOptimalAction(SS);

					delta = R + gamma * QE.getQ(SS, AStar) - QE.getQ(S, A);

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
					steps++;
				}
				
				// Decrement alpha
				this.alpha = this.init_alpha - this.init_alpha * (Math.exp(- (200 / (double) (i+1))));
				// Increment epsilon
				this.epsilon = this.init_epsilon + (1 - this.init_epsilon) * (Math.exp(- (450 / (double) (i+1))));
					
				// Iteration end
				com.onEndIteration(steps, numRandomMoves, i, alpha, epsilon);
				pw.println(i + "\t" + steps + "\t" + numRandomMoves);
				pw.flush();

				com.onFullQUpdate(QE.showQ());

				com.restart();
			}
		} catch (Throwable e) {
			throw e;
		} finally {
			pw.close();
		}
		com.onEndTrain();
	}

	public Action nextOptimalAction(State S) {
		double q = Double.NEGATIVE_INFINITY;
		Action mov = null;
		// Probar movimientos
		for (int i = 0; i < Action.values().length; i++) {
			Action A = new Action(i);
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
				Action A = new Action(r.nextInt(Action.values().length));
				if (S.esAccionValida(A)) {
					mov = A;
				}
			}
		}

		return mov;
	}

}
