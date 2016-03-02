package qLearning.agent.qFunction;

import java.io.FileNotFoundException;

import javax.swing.JPanel;

import qLearning.agent.Action;
import qLearning.agent.state.State;

public interface AbstractQFunction {

	public double get(State S, Action A);
	public void set(State S, Action A, double val);

	public void writeToFile(String file) throws FileNotFoundException;
	public void readFromFile(String file) throws FileNotFoundException;
	
	public JPanel showQ();
	
}
