package qLearning.agent.qFunction;

import java.io.FileNotFoundException;
import java.util.HashMap;
import qLearning.Const;
import qLearning.agent.Action;
import qLearning.agent.state.State;
import qLearning.enviroment.AbstractEnviroment;

public class QMap implements AbstractQFunction {

	private HashMap<Integer, Double> arrayQ;
	private int size;
	private int last;

	public QMap(AbstractEnviroment e) {
		this.size = e.getNumValuesPerDims().stream().reduce(1, (a, b) -> a * b);
		this.last = e.getNumValuesPerDims().get(e.getNumValuesPerDims().size()-1);

		System.out.println("Size of q = " + size);

		this.arrayQ = new HashMap<>();
	}

	private int getHash(State S, Action A) {
		// TODO correct?
		return Long.hashCode(S.hashCode() + last * A.ordinal());
	}

	@Override
	public double get(State S, Action A) {
		Double d = arrayQ.get(getHash(S, A));
		if (d != null)
			return d;
		arrayQ.put(getHash(S, A), Const.Q_GENERAL);
		return Const.Q_GENERAL;
	}

	@Override
	public void set(State S, Action A, double val) {
		arrayQ.put(getHash(S, A), val);
	}

	@Override
	public void writeToFile(String file) throws FileNotFoundException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void readFromFile(String file) throws FileNotFoundException {
		throw new RuntimeException("Not implemented");
	}

}
