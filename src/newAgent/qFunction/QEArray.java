package newAgent.qFunction;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.function.BiFunction;

import javax.swing.JPanel;

import newAgent.AbstractEnvironment;
import newAgent.Action;
import newAgent.Const;
import newAgent.state.State;

public class QEArray implements AbstractQEFunction {

	private int dimsSize;
	private int totalSize;

	private ArrayList<Set> array;

	public QEArray(AbstractEnvironment env) {
		this.dimsSize = env.getNumValuesPerDims().stream().reduce(1, (a, b) -> a * b);

		this.totalSize = dimsSize * Action.values().length;
		this.array = new ArrayList<>(totalSize);

		System.out.println("Size of q = " + totalSize);

		for (int i = 0; i < totalSize; i++) {
			array.add(i, new Set(Const.Q_GENERAL, 0));
		}
	}

	private int getHash(State S, Action A) {
		// TODO correct?
		return Long.hashCode(S.hashCode() + dimsSize * A.ordinal());
	}

	@Override
	public double getQ(State S, Action A) {
		return array.get(getHash(S, A)).q;
	}

	@Override
	public double getE(State S, Action A) {
		return array.get(getHash(S, A)).e;
	}

	@Override
	public void resetE() {
		
		array.replaceAll(set -> {
			set.e = 0;
			return set;
		});
	}

	@Override
	public void setQ(State S, Action A, double q) {
		Set s = array.get(getHash(S, A));
		s.q = q;
		array.set(getHash(S, A), s);
	}

	@Override
	public void setE(State S, Action A, double e) {
		Set s = array.get(getHash(S, A));
		s.e = e;
		array.set(getHash(S, A), s);
	}

	@Override
	public void setAll(State S, Action A, double q, double e) {
		Set s = array.get(getHash(S, A));
		s.q = q;
		s.e = e;
		array.set(getHash(S, A), s);
	}

	@Override
	public void writeToFile(String file) throws FileNotFoundException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void readFromFile(String file) throws FileNotFoundException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public JPanel showQ() {
		return new JPanel();
	}

	@Override
	public void replaceValues(BiFunction<? super Integer, ? super Set, ? extends Set> function) {
		for (int i = 0; i < array.size(); i++) {
			array.set(i, function.apply(i, array.get(i)));
		}
	}

}
