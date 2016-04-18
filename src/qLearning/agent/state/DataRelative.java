package qLearning.agent.state;

import java.util.ArrayList;

import com.Com;

import bot.commonFunctions.Distances;
import bwapi.UnitType;

public class DataRelative extends StateData {

	private static final int MAX_LIFE_DISCRETE = 5;
	private static final int MAX_DISTANCE_DISCRETE = 10;

	// Classes extending Dimension
	// To create a new dimension:
	// 1 - create a new class that extends Dimension, as above
	// 2 - add it to the getNumValuesPerDims static method, as the default
	// dimensions
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

	public class DistanceToNearestEnemy extends Dimension<Double> {
		public DistanceToNearestEnemy(Double rawValue) {
			super(rawValue, MAX_DISTANCE_DISCRETE, "Distance");
		}

		public DistanceToNearestEnemy() {
			this(Distances.getDistanceToNearestEnemy(com.ComData.unit));
		}

		@Override
		public int discretize() {
			return (int) Math.floor(getRawValue() * getMaxDiscreteValue() / UnitType.Terran_Marine.sightRange());
		}

		@Override
		public DistanceToNearestEnemy getNewDimension() {
			double newDistance = getCurrentValueFromBot();
			return new DistanceToNearestEnemy(newDistance);
		}

		@Override
		public Double getCurrentValueFromBot() {
			return Distances.getDistanceToNearestEnemy(com.ComData.unit);
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
		this.dimensions.add(new DistanceToNearestEnemy());
	}
}
