package qLearning.agent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

import com.Com;

import qLearning.Const;
import qLearning.agent.estados.State;
import qLearning.agent.estados.StateContainer;
import qLearning.agent.qFunction.AbstractQFunction;
import qLearning.agent.qFunction.QAarray;
import qLearning.enviroment.AbstractGridEnviroment;

public class Agent implements Runnable {
	private StateContainer estados;
	private AbstractQFunction Q;

	private AbstractGridEnviroment enviroment;
	private Com com;
	private int nume;

	private int sizeX;
	private int sizeY;

	private double epsilon;
	private double alpha;
	private double gamma;

	public Agent(Com com, AbstractGridEnviroment e, double alpha, double gamma, double epsilon) {
		this(com, e);
		this.alpha = alpha;
		this.gamma = gamma;
		this.epsilon = epsilon;
	}
	
	public Agent(Com com, AbstractGridEnviroment e) {
		this.com = com;
		
		sizeX = e.getSizeX();
		sizeY = e.getSizeY();

		this.enviroment = e;
		this.estados = new StateContainer(enviroment, this.com);
		
		this.Q = new QAarray(e, estados);
		
		this.alpha = qLearning.Const.ALPHA;
		this.gamma = qLearning.Const.GAMMA;
		this.epsilon = qLearning.Const.EPSLLON_EGREEDY;
	}

	public void run() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File("results " + "e = " + epsilon + " a = " + alpha + " g = " + gamma));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		com.onSendMessage("q-Learning started");
		for (int i = 0; i < Const.NUM_EPISODIOS; i++) {

			com.Sync.waitForBotGameIsStarted();		
			
			State S = estados.getEstadoInicial();
			int movimientos = 0;
			nume = 0;

			com.Sync.signalAgentIsStarting();
			
			while (!S.isFinalEnd()) {
				Action A = siguienteAccion(S);

				State SS = S.executeAction(A);

				double R = SS.getReward();

				double maxq = Double.NEGATIVE_INFINITY;
				// Probar movimientos
				for (int k = 0; k < Action.values().length; k++) {
					Action a = Action.values()[k];
					// Hasta encontrar uno valido, y ademas sela el mejor
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
			com.onEndIteration(movimientos, nume, i);
			pw.println(i + "\t" + movimientos + "\t" + nume);
			pw.flush();
			com.restart();
		}
		com.onEndTrain();
		pw.close();
		// printPolicy();
	}

	public void printPolicy() {
		for (int j = sizeY - 1; j >= 0; j--) {
			for (int i = 0; i < sizeX; i++) {
				State S = estados.getEstado(i, j);
				// Probar movimientos
				double maxq = Double.NEGATIVE_INFINITY;
				Action A = Action.UP;
				for (int k = 0; k < Action.values().length; k++) {
					Action a = Action.values()[k];
					// Hasta encontrar uno valido, y ademas sela el mejor
					// (greedy)
					if (Q.get(S, a) >= maxq && S.esAccionValida(a)) {
						maxq = Q.get(S, a);
						A = a;
					}
				}
				if (S.isStart())
					System.out.print("|" + "000" + "");
				else if (S.isEnd())
					System.out.print("|" + "XXX" + "");
				else if (enviroment.isValid(i, j))
					System.out.print("|" + A + "");
				else
					System.out.print("|" + "***" + "");
			}
			com.onSendMessage("|");
		}
	}

	public Action siguienteAccion(State S) {

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
			nume++;
			// Tomar un movimiento aleatorio valido
			while (mov == null) {
				Action A = Action.values()[r.nextInt(Action.values().length)];
				if (S.esAccionValida(A)) {
					mov = A;
				}
			}
		}
		return mov;
	}
	
}
