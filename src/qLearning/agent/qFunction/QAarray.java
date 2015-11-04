package qLearning.agent.qFunction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import qLearning.Const;
import qLearning.agent.Action;
import qLearning.agent.estados.State;
import qLearning.agent.estados.StateContainer;
import qLearning.enviroment.AbstractGridEnviroment;

public class QAarray implements AbstractQFunction {

	private double[][][][] arrayQ;
	private int sizeX;
	private int sizeY;

	public QAarray(AbstractGridEnviroment e, StateContainer s) {
		this.sizeX = e.getSizeX();
		this.sizeY = e.getSizeY();

		this.arrayQ = new double[sizeX][sizeY][2][Action.values().length];

		for (int i = 0; i < sizeX; i++) {
			for (int j = 0; j < sizeY; j++) {
				if (s.getEstado(i, j).isEnd()) {
					for (int k = 0; k < Action.values().length; k++) {
						this.arrayQ[i][j][0][k] = 0;
						this.arrayQ[i][j][1][k] = 0;
					}
				} else {
					for (int k = 0; k < Action.values().length; k++) {
						this.arrayQ[i][j][0][k] = Const.Q_GENERAL;
						this.arrayQ[i][j][1][k] = Const.Q_GENERAL;
					}
				}
			}
		}
	}

	@Override
	public double get(State S, Action A) {
		return arrayQ[S.getX() / bot.Const.STEP][S.getY() / bot.Const.STEP][S.getBaliza() ? 1 : 0][A.ordinal()];
	}

	@Override
	public void set(State S, Action A, double val) {
		arrayQ[S.getX() / bot.Const.STEP][S.getY() / bot.Const.STEP][S.getBaliza() ? 1 : 0][A.ordinal()] = val;
	}

	@Override
	public void writeToFile(String file) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(new File(file));

		for (int i = 0; i < sizeX; i++) {
			for (int j = 0; j < sizeY; j++) {
				for (int k = 0; k < Action.values().length; k++) {
					pw.print(this.arrayQ[i][j][0][k]);
					pw.print(this.arrayQ[i][j][1][k]);
				}
			}
			pw.println("");
		}

		pw.close();
	}

	@Override
	public void readFromFile(String file) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(file));

		for (int i = 0; i < sizeX; i++) {
			for (int j = 0; j < sizeY; j++) {
				for (int k = 0; k < Action.values().length; k++) {
					(this.arrayQ[i][j][0][k]) = sc.nextDouble();
					(this.arrayQ[i][j][1][k]) = sc.nextDouble();
				}
			}
		}

		sc.close();
	}

}
