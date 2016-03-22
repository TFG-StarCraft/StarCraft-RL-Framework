package qLearning.agent.state;

import java.util.ArrayList;

import com.Com;

import bwapi.UnitType;

public class DataRelative extends StateData {

	private static final int MAX_LIFE_DISCRETE = 5;
	private static final int MAX_DISTANCE_DISCRETE = 5;

	public class MyLife extends Dimension<Integer> {

		public MyLife(Integer rawValue) {
			super(rawValue, MAX_LIFE_DISCRETE, "MyLife");
		}

		public MyLife() {
			this(com.ComData.unit.getHitPoints());
			System.out.println("HP: " + com.ComData.unit.getHitPoints()+ ", " + discretize());
		}

		@Override
		public int discretize() {
			return (int) Math.floor(getRawValue() * getMaxDiscreteValue() / UnitType.Terran_Marine.maxHitPoints());
		}

		@Override
		public MyLife getNewDimension() {
			int newLife = (int) Math.floor(com.ComData.unit.getHitPoints() * getMaxDiscreteValue() / UnitType.Terran_Marine.maxHitPoints());
			return new MyLife(newLife);
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
			double newDistance = Math.floor(com.ComData.getDistance() * getMaxDiscreteValue() / UnitType.Terran_Marine.sightRange());
			return new Distance(newDistance);
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
		
		this.dimensions = new ArrayList<>(2);

		this.dimensions.add(new MyLife());
		this.dimensions.add(new Distance());
	}
}
