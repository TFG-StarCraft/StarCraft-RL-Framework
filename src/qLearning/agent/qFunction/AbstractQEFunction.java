package qLearning.agent.qFunction;

import java.io.FileNotFoundException;

import javax.swing.JPanel;

import qLearning.agent.Action;
import qLearning.agent.state.State;

public interface AbstractQEFunction {

	double getQ(State S, Action A);
	double getE(State S, Action A);

	void resetE();

	void setQ(State S, Action A, double q);
	void setE(State S, Action A, double e);
	void setAll(State S, Action A, double q, double e);

	void writeToFile(String file) throws FileNotFoundException;
	void readFromFile(String file) throws FileNotFoundException;

	JPanel showQ();

}