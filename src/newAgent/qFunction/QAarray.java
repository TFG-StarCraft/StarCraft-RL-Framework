package newAgent.qFunction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JPanel;

import newAgent.AbstractEnvironment;
import newAgent.Action;
import newAgent.state.State;

public class QAarray implements AbstractQFunction {

	private double[][] arrayQ;
	private int size;

	public QAarray(AbstractEnvironment e) {
		throw new RuntimeException("NOTIMPLEMENTED");
		/*
		this.size = e.getNumValuesPerDims().stream().reduce(1, (a, b) -> a * b);
		this.sizes = e.getNumValuesPerDims();

		System.out.println("Size of q = " + size);

		this.arrayQ = new double[size][Action.values().length];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < Action.values().length; j++) {
				this.arrayQ[i][j] = Const.Q_GENERAL;
			}
		}*/
	}

	@Override
	public double get(State S, Action A) {
		int stride = 0;
		/*
		 * stride =
		 * 
		 * [(int) Math.floor(S.getMyLife() * 9 /
		 * UnitType.Terran_Marine.maxHitPoints())] [(int)
		 * Math.floor(S.getDistance() * 9 /
		 * UnitType.Terran_Marine.sightRange())]
		 */
		return arrayQ[stride][A.ordinal()];
	}

	@Override
	public void set(State S, Action A, double val) {
		int stride = 0;/*
						 * [(int) Math.floor(S.getMyLife() * 9 /
						 * UnitType.Terran_Marine.maxHitPoints())] [(int)
						 * Math.floor(S.getDistance() * 9 /
						 * UnitType.Terran_Marine.sightRange())]
						 */

		arrayQ[stride][A.ordinal()] = val;
	}

	@Override
	public void writeToFile(String file) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(new File(file));

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < Action.values().length; j++) {
				pw.print(this.arrayQ[i][j]);
			}
			pw.println("");
		}

		pw.close();
	}

	@Override
	public void readFromFile(String file) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(file));

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < Action.values().length; j++) {
				(this.arrayQ[i][j]) = sc.nextDouble();
			}
		}

		sc.close();
	}

	@Override
	public JPanel showQ() {
		// TODO Auto-generated method stub
		return null;
	}

}
