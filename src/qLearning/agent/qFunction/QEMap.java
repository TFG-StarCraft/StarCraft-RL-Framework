package qLearning.agent.qFunction;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.function.BiFunction;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import qLearning.Const;
import qLearning.agent.Action;
import qLearning.agent.state.State;
import qLearning.environment.AbstractEnvironment;

public class QEMap extends HashMap<Integer, AbstractQEFunction.Set> implements AbstractQEFunction {

	private static final long serialVersionUID = 2792701869347588301L;

	private int dimsSize;
	private AbstractEnvironment env;

	public QEMap(AbstractEnvironment env) {
		this.dimsSize = env.getNumValuesPerDims().stream().reduce(1, (a, b) -> a * b);
		this.env = env;

		System.out.println("Size of q = " + dimsSize * Action.values().length);

		// TODO
		for (int i = 0; i < dimsSize * Action.values().length; i++) {
			this.put(i, new Set(Const.Q_GENERAL, 0));
		}

	}

	private int getHash(State S, Action A) {
		// TODO correct?
		return Long.hashCode(S.hashCode() + dimsSize * A.ordinal());
	}

	@Override
	public double getQ(State S, Action A) {
		Set set = this.get(getHash(S, A));
/*
		if (set == null) {
			set = new Set(Const.Q_GENERAL, 0, S, A);
			this.put(getHash(S, A), set);
		}
*/
		return set.q;
	}

	@Override
	public double getE(State S, Action A) {
		Set set = this.get(getHash(S, A));
/*
		if (set == null) {
			set = new Set(Const.Q_GENERAL, 0, S, A);
			this.put(getHash(S, A), set);
		}
*/
		return set.e;
	}

	@Override
	public void resetE() {
		this.replaceAll((k, v) -> {
			v.e = 0;
			return v;
		});
	}

	@Override
	public void setQ(State S, Action A, double q) {
		double oldE = this.get(getHash(S, A)).e;
		this.put(getHash(S, A), new Set(q, oldE, S, A));
	}

	@Override
	public void setE(State S, Action A, double e) {
		double oldQ = this.get(getHash(S, A)).q;
		this.put(getHash(S, A), new Set(oldQ, e, S, A));
	}

	@Override
	public void setAll(State S, Action A, double q, double e) {
		this.put(getHash(S, A), new Set(q, e, S, A));
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

	// TODO
	private class QCell extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2526531823051036225L;

		private QCell() {
			super(new GridLayout(Action.values().length, 1));
			for (int i = 0; i < Action.values().length; i++) {
				JLabel l = new JLabel("   ");
				this.add(l);
				l.setForeground(Color.gray);
			}

			this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}

		private void set(int n, Set s) {
			((JLabel) getComponent(n)).setText(df.format(s.q));
			
			if (s.q > s.oldq) {
				((JLabel) getComponent(n)).setForeground(Color.green);
			} else if (s.q < s.oldq) {
				((JLabel) getComponent(n)).setForeground(Color.red);
			} else {
				((JLabel) getComponent(n)).setForeground(Color.black);
			}
			
			repaint();
		}

	}

	// TODO
	@Override
	public JPanel showQ() {

		if (env.getNumDims() != 2) {
			for (Entry<Integer, Set> e : entrySet()) {
				if (e.getValue().s != null) {
					System.out.println("Action: " + e.getValue().a.toString());
					System.out.println("State: " + e.getValue().s.toString());
					System.out.println("Q: " + e.getValue().q);
					System.out.println("E: " + e.getValue().e);
				}
			}
		}
		if (env.getNumDims() == 2) {
			int dx = env.getNumValuesPerDims().get(0), dy = env.getNumValuesPerDims().get(1);
			JPanel panel = new JPanel(new GridLayout(dx + 1, dy + 1));

			for (int i = 0; i <= dx; i++) {
				for (int j = 0; j <= dy; j++) {
					panel.add(new QCell(), i * dy + j);
				}
			}

			for (Entry<Integer, Set> e : entrySet()) {
				if (e.getValue().s != null) {
					int x = e.getValue().s.getData().getValues().get(0).discretize();
					int y = e.getValue().s.getData().getValues().get(1).discretize();
					int z = e.getValue().a.ordinal();

					QCell cell = (QCell) panel.getComponent(x * dy + y);

					cell.set(z, e.getValue());
				}
			}

			return panel;
		}

		return new JPanel();
	}

	@Override
	public void replaceValues(BiFunction<? super Integer, ? super Set, ? extends Set> function) {
		replaceAll(function);
	}

}
