package qLearning.agent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

import com.Com;

import qLearning.Const;
import qLearning.agent.state.State;
import qLearning.enviroment.AbstractEnviroment;

public class LambdaQ extends AbstractAlgorithm {
	
	private double lambda;

	public LambdaQ(Com com, AbstractEnviroment e, double alpha, double gamma, double epsilon, double lambda) {
		super(com, e, alpha, gamma, epsilon);
		
		this.lambda = lambda;
	}

	public LambdaQ(Com com, AbstractEnviroment e) {
		super(com, e);
		
		this.lambda = 0.1;
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

			State S = enviroment.getInitState();
			Action A = nextAction(S, i);
			int movimientos = 0;
			this.numRandomMoves = 0;
			this.Q.resetE();

			// com.Sync.signalAgentIsStarting();

			while (!S.isFinalEnd()) {
				// Blocks until action A ends
				Action AA, AStar;
				double delta;
				
				double E;
				
				State SS = S.executeAction(A);

				Double R = SS.getReward();
				com.onDebugMessage(R.toString(), utils.DebugEnum.REWARD);
				
				AA = nextAction(SS, i);
				AStar = nextOptimalAction(SS, i);
				
				delta = R + gamma * Q.getQ(SS, AStar) - Q.getQ(S, A);
				
				E = Q.getE(S, A) + 1;
				// E = 1;
				// E = (1 - alpha) * Q.getE(S, A) + 1;
				
				Q.bucle(alpha, delta, gamma, lambda, AA, AStar);			

				S = SS;
				A = AA;
				movimientos++;
			}
			// Iteration end
			com.onEndIteration(movimientos, numRandomMoves, i);
			pw.println(i + "\t" + movimientos + "\t" + numRandomMoves);
			pw.flush();

			com.onFullQUpdate(Q.showQ());

			com.restart();
		}
		com.onEndTrain();
		pw.close();
	}

	public Action nextOptimalAction(State S, int epoch) {
		double q = Double.NEGATIVE_INFINITY;
		Action mov = null;
		// Probar movimientos
		for (int i = 0; i < Action.values().length; i++) {
			Action A = Action.values()[i];
			// Hasta encontrar uno valido, y ademas sela el mejor (greedy)
			if (Q.getQ(S, A) >= q && S.esAccionValida(A)) {
				q = Q.getQ(S, A);
				mov = Action.values()[i];
			}
		}

		return mov;
	}

	public Action nextAction(State S, int epoch) {

		Random r = new Random();
		double e = r.nextDouble();
		Action mov = null;

		if (e < epsilon) {
			mov = nextOptimalAction(S, epoch);
		} else {
			numRandomMoves++;
			// Tomar un movimiento aleatorio valido
			while (mov == null) {
				Action A = Action.values()[r.nextInt(Action.values().length)];
				if (S.esAccionValida(A)) {
					mov = A;
				}
			}
		}

		mov.epoch = epoch;

		return mov;
	}

}
