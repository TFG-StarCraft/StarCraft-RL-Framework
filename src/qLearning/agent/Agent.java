package qLearning.agent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import com.Com;

import qLearning.Const;
import qLearning.agent.qFunction.AbstractQFunction;
import qLearning.agent.qFunction.QMap;
import qLearning.agent.state.State;
import qLearning.enviroment.AbstractEnviroment;

public class Agent implements Runnable {
	private AbstractQFunction Q;

	private AbstractEnviroment enviroment;
	private Com com;
	private int numRandomMoves;

	private double epsilon;
	private double alpha;
	private double gamma;

	public Agent(Com com, AbstractEnviroment e, double alpha, double gamma, double epsilon) {
		this(com, e);
		this.alpha = alpha;
		this.gamma = gamma;
		this.epsilon = epsilon;
	}
	
	public Agent(Com com, AbstractEnviroment e) {
		this.com = com;

		this.enviroment = e;
		
		this.Q = new QMap(e);
		
		this.alpha = qLearning.Const.ALPHA;
		this.gamma = qLearning.Const.GAMMA;
		this.epsilon = qLearning.Const.EPSLLON_EGREEDY;
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
			int movimientos = 0;
			this.numRandomMoves = 0;

			//com.Sync.signalAgentIsStarting();
			
			while (!S.isFinalEnd()) {
				Action A = nextAction(S, i);

				// Blocks until action A ends
				State SS = S.executeAction(A);

				Double R = SS.getReward();
				com.onDebugMessage(R.toString(), utils.DebugEnum.REWARD);
				double maxq = Double.NEGATIVE_INFINITY;
				// Probar movimientos
				for (int k = 0; k < Action.values().length; k++) {
					Action a = Action.values()[k];
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
			com.onEndIteration(movimientos, numRandomMoves, i);
			pw.println(i + "\t" + movimientos + "\t" + numRandomMoves);
			pw.flush();
			
			/*
			Q.showQ();
			try {
				System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			com.restart();
		}
		com.onEndTrain();
		pw.close();
	}

	public Action nextAction(State S, int epoch) {

		Random r = new Random();
		double e = r.nextDouble();
		Action mov = null;

		if (e < epsilon) {
			double q = Double.NEGATIVE_INFINITY;

			// Probar movimientos
			for (int i = 0; i < Action.values().length; i++) {
				Action A = Action.values()[i];
				// Hasta encontrar uno valido, y ademas sela el mejor (greedy)
				if (Q.get(S, A) >= q && S.esAccionValida(A)) {
					q = Q.get(S, A);
					mov = Action.values()[i];
				}
			}
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
