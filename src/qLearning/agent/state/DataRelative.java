package qLearning.agent.state;

import java.util.ArrayList;

import com.Com;

import bwapi.UnitType;

public class DataRelative extends StateData {

	private static final int MAX_LIFE_DISCRETE = 5;
	private static final int MAX_DISTANCE_DISCRETE = 10;

	// Classes extending Dimension
	// To create a new dimension:
	// 1 - create a new class that extends Dimension, as above
	// 2 - add it to the getNumValuesPerDims static method, as the default dimensions
	// 3 - add it to the DataRelative constructor
	
	public class MyLife extends Dimension<Integer> {

		public MyLife(Integer rawValue) {
			super(rawValue, MAX_LIFE_DISCRETE, "MyLife");
		}

		public MyLife() {
			this(com.ComData.unit.getHitPoints());
		}

		@Override
		public int discretize() {
			return (int) Math.floor(getRawValue() * getMaxDiscreteValue() / UnitType.Terran_Marine.maxHitPoints());
		}

		@Override
		public MyLife getNewDimension() {
			int newLife = com.ComData.unit.getHitPoints();
			return new MyLife(newLife);
		}

		@Override
		public Integer getCurrentValueFromBot() {
			return com.ComData.unit.getHitPoints();
		}
	}

	public class Distance extends Dimension<Double> {
		public Distance(Double rawValue) {
			super(rawValue, MAX_DISTANCE_DISCRETE, "Distance");
		}

		public Distance() {
			this(com.ComData.getDistance());
		}

		@Override
		public int discretize() {
			return (int) Math.floor(getRawValue() * getMaxDiscreteValue() / UnitType.Terran_Marine.sightRange());
		}

		@Override
		public Distance getNewDimension() {
			double newDistance = getCurrentValueFromBot();
			return new Distance(newDistance);
		}

		@Override
		public Double getCurrentValueFromBot() {
			return com.ComData.getDistance();
		}
	}

	public static ArrayList<Integer> getNumValuesPerDims() {
		ArrayList<Integer> a = new ArrayList<>();
		
		a.add(MAX_LIFE_DISCRETE);
		a.add(MAX_DISTANCE_DISCRETE);
		
		return a;
	}
	
	public DataRelative(Com com) {
		this.com = com;
		
		this.dimensions = new ArrayList<>();

		this.dimensions.add(new MyLife());
		this.dimensions.add(new Distance());
	}
}
