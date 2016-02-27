package qLearning.agent.state;

public abstract class Dimension<T> {

	private T value;
	private int max;

	public Dimension(T value, int max) {
		this.value = value;
		this.max = max;
	}

	public T getValue() {
		return value;
	}
	
	public abstract Dimension<T> getNewDimension();
	
	public abstract int discretize();

	public int getMaxDiscreteValue() {
		return max;
	}
	
	public int getNumOfValues() {
		return max+1;
	}

}
