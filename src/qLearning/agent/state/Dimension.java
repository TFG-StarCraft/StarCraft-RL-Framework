package qLearning.agent.state;

public abstract class Dimension<T> {

	private T value;
	private int max;
	private String name;
	
	public Dimension(T value, int max, String name) {
		this.value = value;
		this.max = max;
		this.name = name;
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

	public String getName() {
		return name;
	}

}
