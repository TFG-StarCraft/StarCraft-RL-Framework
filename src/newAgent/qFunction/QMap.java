package newAgent.qFunction;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import newAgent.AbstractEnvironment;
import newAgent.Action;
import newAgent.Const;
import newAgent.state.Dimension;
import newAgent.state.State;

public class QMap extends HashMap<Integer, QMap.Set> implements AbstractQFunction {

	private static final long serialVersionUID = 6999001742786415696L;

	private int dimsSize;
	private AbstractEnvironment env;

	public class Set {
		public State s;
		public Action a;
		public double q;

		public Set(State s, Action a, double q) {
			this.q = q;
			this.s = s;
			this.a = a;
		}
	}

	public QMap(AbstractEnvironment env) {
		this.env = env;
		this.dimsSize = env.getNumValuesPerDims().stream().reduce(1, (a, b) -> a * b);
		System.out.println("Size of q = " + dimsSize * Action.values().length);
	}

	private int getHash(State S, Action A) {
		return Long.hashCode(S.hashCode() + dimsSize * A.ordinal());
	}

	@Override
	public double get(State S, Action A) {
		Set set = this.get(getHash(S, A));
		if (set == null)
			return Const.Q_GENERAL;
		return set.q;
	}

	@Override
	public void set(State S, Action A, double val) {
		this.put(getHash(S, A), new Set(S, A, val));
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
				this.add(new JLabel("" + newAgent.Const.Q_GENERAL));
			}

			this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}

		private void set(int n, double d) {
			((JLabel) getComponent(n)).setText(df.format(d));
			repaint();
		}

	}

	public JPanel showQ() {

		if (env.getNumDims() > 2) {
			for (Entry<Integer, Set> e : this.entrySet()) {
				System.out.println("Action: " + e.getValue().a.toString());
				for (Dimension<?> ee : e.getValue().s.getData().getValues()) {
					System.out.println(ee.getName() + ": " + ee.discretize());
				}
				System.out.println("Q: " + e.getValue().q);
			}

			return null;
		} else if (env.getNumDims() == 2) {
			int dx = env.getNumValuesPerDims().get(0), dy = env.getNumValuesPerDims().get(1);
			JPanel panel = new JPanel(new GridLayout(dx, dy));

			for (int i = 0; i < dx; i++) {
				for (int j = 0; j < dy; j++) {
					panel.add(new QCell(), i * dy + j);
				}
			}

			for (Entry<Integer, Set> e : this.entrySet()) {
				int x = e.getValue().s.getData().getValues().get(0).discretize();
				int y = e.getValue().s.getData().getValues().get(1).discretize();
				int z = e.getValue().a.ordinal();

				QCell cell = (QCell) panel.getComponent(x * dy + y);

				cell.set(z, e.getValue().q);
			}

			return panel;
		}

		return null;
	}

}