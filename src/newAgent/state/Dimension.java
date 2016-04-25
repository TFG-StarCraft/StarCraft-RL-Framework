package newAgent.state;

public abstract class Dimension<T> {

	private T value;
	private int max;
	private String name;

	/**
	 * 
	 * @param rawValue
	 *            rawValue of the dimension, NOT DISCRETIZED
	 * @param max
	 *            max discrete value. Discrete values will be Natural numbers,
	 *            in the interval [0, max], max included
	 * @param name
	 */
	public Dimension(T rawValue, int max, String name) {
		this.value = rawValue;
		this.max = max;
		this.name = name;
	}

	public T getRawValue() {
		return value;
	}

	public abstract Dimension<T> getNewDimension();

	public abstract int discretize();
	
	public abstract T getCurrentValueFromBot();

	public int getMaxDiscreteValue() {
		return max;
	}

	public int getNumOfValues() {
		return max + 1;
	}

	public String getName() {
		return name;
	}

}
