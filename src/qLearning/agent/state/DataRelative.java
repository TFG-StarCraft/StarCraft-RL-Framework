package qLearning.agent.state;

import java.util.ArrayList;

import com.Com;

import bwapi.UnitType;

public class DataRelative extends StateData {

	private static final int MAX_LIFE_DISCRETE = 9;
	private static final int MAX_DISTANCE_DISCRETE = 9;

	public class MyLife extends Dimension<Integer> {

		public MyLife(Integer value) {
			super(value, MAX_LIFE_DISCRETE);
		}

		@Override
		public int discretize() {
			return (int) Math.floor(getValue() * getMaxDiscreteValue() / UnitType.Terran_Marine.maxHitPoints());
		}

		@Override
		public MyLife getNewDimension() {
			int newLife = (int) Math.floor(com.ComData.unit.getHitPoints() * 9 / UnitType.Terran_Marine.maxHitPoints());
			return new MyLife(newLife);
		}
	}

	public class Distance extends Dimension<Double> {
		public Distance(Double value) {
			super(value, MAX_DISTANCE_DISCRETE);
		}

		@Override
		public int discretize() {
			return (int) Math.floor(getValue() * getMaxDiscreteValue() / UnitType.Terran_Marine.sightRange());
		}

		@Override
		public Distance getNewDimension() {
			double newDistance = Math.floor(com.ComData.getDistance() * 9 / UnitType.Terran_Marine.sightRange());
			return new Distance(newDistance);
		}
	}

	public DataRelative(Com com) {
		this(MAX_LIFE_DISCRETE, MAX_DISTANCE_DISCRETE, com);
	}

	public DataRelative(int life, double distance, Com com) {
		this.dimensions = new ArrayList<>(2);

		this.dimensions.add(new MyLife(life));
		this.dimensions.add(new Distance(distance));

		this.com = com;
	}
}
