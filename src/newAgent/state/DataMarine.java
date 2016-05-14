package newAgent.state;

import java.util.ArrayList;

import com.Com;

import bot.commonFunctions.Distances;
import bwapi.Unit;
import bwapi.UnitType;

public class DataMarine extends StateData {

	private static final int MAX_LIFE_DISCRETE = 9;
	private static final int MAX_DISTANCE_DISCRETE = 9;

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
			this(unit.getHitPoints());
		}

		@Override
		protected int impDiscretize() {
			return (int) Math.floor(getRawValue() * getMaxDiscreteValue() / UnitType.Terran_Marine.maxHitPoints());
		}

		@Override
		public MyLife getNewDimension() {
			int newLife = unit.getHitPoints();
			return new MyLife(newLife);
		}

		@Override
		public Integer getCurrentValueFromBot() {
			return unit.getHitPoints();
		}
	}

	public class DistanceToNearestEnemy extends Dimension<Double> {
		public DistanceToNearestEnemy(Double rawValue) {
			super(rawValue, MAX_DISTANCE_DISCRETE, "Distance");
		}

		public DistanceToNearestEnemy() {
			this(Distances.getDistanceToNearestEnemy(unit));
		}

		@Override
		protected int impDiscretize() {
			return (int) Math.floor(getRawValue() * getMaxDiscreteValue() / UnitType.Terran_Marine.sightRange());
		}

		@Override
		public DistanceToNearestEnemy getNewDimension() {
			double newDistance = getCurrentValueFromBot();
			return new DistanceToNearestEnemy(newDistance);
		}

		@Override
		public Double getCurrentValueFromBot() {
			return Distances.getDistanceToNearestEnemy(unit);
		}
	}

	public static ArrayList<Integer> getNumValuesPerDims() {
		ArrayList<Integer> a = new ArrayList<>();

		a.add(MAX_LIFE_DISCRETE + 1);
		a.add(MAX_DISTANCE_DISCRETE + 1);

		return a;
	}

	public static int getNumDims() {
		return getNumValuesPerDims().size();
	}

	private Unit unit;

	public DataMarine(Com com, Unit unit) {
		this.com = com;
		this.unit = unit;

		this.dimensions = new ArrayList<>();

		this.dimensions.add(new MyLife());
		this.dimensions.add(new DistanceToNearestEnemy());
	}

	@Override
	public StateData getNewStateData() {
		DataMarine r = new DataMarine(com, unit);

		r.dimensions.clear();

		for (Dimension<?> dimension : dimensions) {
			r.dimensions.add(dimension.getNewDimension());
		}

		return r;
	}

}
