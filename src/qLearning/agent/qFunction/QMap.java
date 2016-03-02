package qLearning.agent.qFunction;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map.Entry;

import qLearning.Const;
import qLearning.agent.Action;
import qLearning.agent.state.Dimension;
import qLearning.agent.state.State;
import qLearning.enviroment.AbstractEnviroment;

public class QMap implements AbstractQFunction {

	private HashMap<Integer, Set> arrayQ;
	private int size;
	private int last;
	
	private class Set {
		private State s;
		private Action a;
		private double Q;
		
		private Set(State s, Action a, double Q) {
			this.Q = Q;
			this.s = s;
			this.a = a;
		}
	}

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
		Set set = arrayQ.get(getHash(S, A));
		if (set == null)
			return Const.Q_GENERAL;
		return set.Q;
		//arrayQ.put(getHash(S, A), newConst.Q_GENERAL);
	}

	@Override
	public void set(State S, Action A, double val) {
		arrayQ.put(getHash(S, A), new Set(S, A, val));
	}

	@Override
	public void writeToFile(String file) throws FileNotFoundException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void readFromFile(String file) throws FileNotFoundException {
		throw new RuntimeException("Not implemented");
	}

	public void showQ() {
		for (Entry<Integer, Set> e : arrayQ.entrySet()) {
			System.out.println("Action: " + e.getValue().a.toString());
			for (Dimension<?> ee : e.getValue().s.getData().getValues()) {
				System.out.println(ee.getName() + ": " + ee.discretize());
			}
			System.out.println("Q: " +  e.getValue().Q);
		}
	}
	
}
