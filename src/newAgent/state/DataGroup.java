package newAgent.state;

import java.util.ArrayList;

import bwapi.UnitType;
import newAgent.agent.group.MarineGroupAgent;

public class DataGroup extends StateData {

	private static final int MAX_X = 256;
	private static final int TILE_X = 16;
	private static final int MAX_Y = 256;
	private static final int TILE_Y = 16;
	
	public class X extends Dimension<Integer> {

		public X(Integer rawValue) {
			super(rawValue, MAX_X, "X");
		}

		@Override
		public Dimension<Integer> getNewDimension() {
			return new X(agent.getAveragePos().getX());
		}

		@Override
		protected int impDiscretize() {
			return (int) Math.floor(getRawValue() * getMaxDiscreteValue() / TILE_X);
		}

		@Override
		public Integer getCurrentValueFromBot() {
			return agent.getAveragePos().getX();
		}
		
	}
	
	
	public class Y extends Dimension<Integer> {

		public Y(Integer rawValue) {
			super(rawValue, MAX_Y, "Y");
		}

		@Override
		public Dimension<Integer> getNewDimension() {
			return new Y(agent.getAveragePos().getX());
		}

		@Override
		protected int impDiscretize() {
			return (int) Math.floor(getRawValue() * getMaxDiscreteValue() / TILE_Y);
		}

		@Override
		public Integer getCurrentValueFromBot() {
			return agent.getAveragePos().getY();
		}
		
	}
	

	public static ArrayList<Integer> getNumValuesPerDims() {
		ArrayList<Integer> a = new ArrayList<>();

		a.add(MAX_X + 1);
		a.add(MAX_Y + 1);

		return a;
	}

	
	public static int getNumDims() {
		return getNumValuesPerDims().size();
	}
	
	private MarineGroupAgent agent;
	
	public DataGroup(MarineGroupAgent agent) {
		this.dimensions = new ArrayList<>();
		this.agent = agent;

		this.dimensions.add(new X(0));
		this.dimensions.add(new Y(0));
	}
	
	@Override
	public StateData getNewStateData() {
		// TODO Auto-generated method stub
		DataGroup r = new DataGroup(agent);
		
		r.dimensions.clear();

		for (Dimension<?> dimension : dimensions) {
			r.dimensions.add(dimension.getNewDimension());
		}
		
		return r;
	}

}
