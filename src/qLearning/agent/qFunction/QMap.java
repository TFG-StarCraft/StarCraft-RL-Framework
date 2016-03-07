package qLearning.agent.qFunction;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import qLearning.Const;
import qLearning.agent.Action;
import qLearning.agent.state.Dimension;
import qLearning.agent.state.State;
import qLearning.enviroment.AbstractEnviroment;

public class QMap implements AbstractQFunction {

	private HashMap<Integer, Set> arrayQ;
	private int size;
	private int last;
	private AbstractEnviroment env;

	private class Set {
		// private State s;
		// private Action a;
		private double q;
		private double e;

		// private Set(State s, Action a, double q, double e) {
		private Set(double q, double e) {
			this.q = q;
			// this.s = s;
			// this.a = a;
			this.e = e;
		}
	}

	public QMap(AbstractEnviroment env) {
		this.env = env;
		this.size = env.getNumValuesPerDims().stream().reduce(1, (a, b) -> a * b);
		this.last = env.getNumValuesPerDims().get(env.getNumValuesPerDims().size() - 1);

		System.out.println("Size of q = " + size * Action.values().length);

		this.arrayQ = new HashMap<>();

		// TODO
		for (int i = 0; i < 100000; i++) {
			this.arrayQ.put(i, new Set(Const.Q_GENERAL, 0));
		}

	}

	private int getHash(State S, Action A) {
		// TODO correct?
		return Long.hashCode(S.hashCode() + last * A.ordinal());
	}

	@Override
	public double getQ(State S, Action A) {
		Set set = arrayQ.get(getHash(S, A));

		if (set == null) {
			set = new Set(Const.Q_GENERAL, 0);
			arrayQ.put(getHash(S, A), set);
		}

		return set.q;
	}

	public double getE(State S, Action A) {
		Set set = arrayQ.get(getHash(S, A));

		if (set == null) {
			set = new Set(Const.Q_GENERAL, 0);
			arrayQ.put(getHash(S, A), set);
		}

		return set.e;
	}

	public void resetE() {
		arrayQ.replaceAll((k, v) -> {
			v.e = 0;
			return v;
		});
	}

	public void bucle(double alpha, double delta, double gamma, double lambda, Action AA, Action AStar) {
		arrayQ.replaceAll((k, v) -> {
			v.q = v.q + alpha * delta * v.e;
			if (AA.equals(AStar)) {
				v.e = gamma * lambda * v.e;
			} else {
				v.e = 0;
			}
			
			return v;
		});
	}

	@Override
	public void set(State S, Action A, double val) {
		arrayQ.put(getHash(S, A), new Set(val, 0));
	}

	public void setAll(State S, Action A, double q, double e) {
		arrayQ.put(getHash(S, A), new Set(q, e));
	}

	@Override
	public void writeToFile(String file) throws FileNotFoundException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void readFromFile(String file) throws FileNotFoundException {
		throw new RuntimeException("Not implemented");
	}

	private static final DecimalFormat df = new DecimalFormat("0.#####E0");

	private class QCell extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2526531823051036225L;

		private QCell() {
			super(new GridLayout(Action.values().length, 1));
			for (int i = 0; i < Action.values().length; i++) {
				this.add(new JLabel("" + qLearning.Const.Q_GENERAL));
			}

			this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}

		private void set(int n, double d) {
			((JLabel) getComponent(n)).setText(df.format(d));
			repaint();
		}

	}

	public JPanel showQ() {
		/*
		 * if (env.getNumDims() > 2) { for (Entry<Integer, Set> e :
		 * arrayQ.entrySet()) { System.out.println("Action: " +
		 * e.getValue().a.toString()); for (Dimension<?> ee :
		 * e.getValue().s.getData().getValues()) {
		 * System.out.println(ee.getName() + ": " + ee.discretize()); }
		 * System.out.println("Q: " + e.getValue().q); }
		 * 
		 * return null; } else if (env.getNumDims() == 2) { int dx =
		 * env.getNumValuesPerDims().get(0), dy =
		 * env.getNumValuesPerDims().get(1); JPanel panel = new JPanel(new
		 * GridLayout(dx, dy));
		 * 
		 * for (int i = 0; i < dx; i++) { for (int j = 0; j < dy; j++) {
		 * panel.add(new QCell(), i * dy + j); } }
		 * 
		 * for (Entry<Integer, Set> e : arrayQ.entrySet()) { int x =
		 * e.getValue().s.getData().getValues().get(0).discretize(); int y =
		 * e.getValue().s.getData().getValues().get(1).discretize(); int z =
		 * e.getValue().a.ordinal();
		 * 
		 * QCell cell = (QCell) panel.getComponent(x * dy + y);
		 * 
		 * cell.set(z, e.getValue().q); }
		 * 
		 * return panel; }
		 */
		return new JPanel();

	}

}
