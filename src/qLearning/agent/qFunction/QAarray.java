package qLearning.agent.qFunction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import bwapi.UnitType;
import qLearning.Const;
import qLearning.agent.Action;
import qLearning.agent.State;
import qLearning.enviroment.AbstractEnviroment;

public class QAarray implements AbstractQFunction {

	private double[][][] arrayQ;
	private int sizeX;

	public QAarray(AbstractEnviroment e) {
		this.sizeX = 10;

		this.arrayQ = new double[sizeX][sizeX][Action.values().length];

		for (int i = 0; i < sizeX; i++) {
			for (int j = 0; j < sizeX; j++) {
				for (int k = 0; k < Action.values().length; k++) {
					this.arrayQ[i][j][k] = Const.Q_GENERAL;
				}
			}
		}
	}

	@Override
	public double get(State S, Action A) {
		return arrayQ[(int)Math.floor(S.getMyLife()*9/UnitType.Terran_Marine.maxHitPoints())][(int)Math.floor(S.getDistance()*9/UnitType.Terran_Marine.sightRange())][A.ordinal()];
	}

	@Override
	public void set(State S, Action A, double val) {
		arrayQ[(int)Math.floor(S.getMyLife()*9/UnitType.Terran_Marine.maxHitPoints())][(int)Math.floor(S.getDistance()*9/UnitType.Terran_Marine.sightRange())][A.ordinal()] = val;
	}

	@Override
	public void writeToFile(String file) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(new File(file));

		for (int i = 0; i < sizeX; i++) {
				for (int k = 0; k < Action.values().length; k++) {
					pw.print(this.arrayQ[i][k]);
				}
			pw.println("");
		}

		pw.close();
	}

	@Override
	public void readFromFile(String file) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(file));

		for (int i = 0; i < sizeX; i++) {
			for(int j = 0; j < sizeX; j++){
				for (int k = 0; k < Action.values().length; k++) {
					(this.arrayQ[i][j][k]) = sc.nextDouble();
				}
			}
		}

		sc.close();
	}

}
