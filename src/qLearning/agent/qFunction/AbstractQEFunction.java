package qLearning.agent.qFunction;

import java.io.FileNotFoundException;
import java.util.function.BiFunction;

import javax.swing.JPanel;

import qLearning.agent.Action;
import qLearning.agent.state.State;

public interface AbstractQEFunction {
	
	class Set {
		public State s;
		public Action a;
		public double q;
		public double e;

		// private Set(State s, Action a, double q, double e) {
		Set(double q, double e, State s, Action a) {
			this.q = q;
			this.s = s;
			this.a = a;
			this.e = e;
		}
		
		Set(double q, double e) {
			this.q = q;
			this.s = null;
			this.a = null;
			this.e = e;
		}
	}

	double getQ(State S, Action A);
	double getE(State S, Action A);

	void resetE();

	void setQ(State S, Action A, double q);
	void setE(State S, Action A, double e);
	void setAll(State S, Action A, double q, double e);

	void writeToFile(String file) throws FileNotFoundException;
	void readFromFile(String file) throws FileNotFoundException;

	JPanel showQ();
	void replaceValues(BiFunction<? super Integer, ? super Set, ? extends Set> function);

}