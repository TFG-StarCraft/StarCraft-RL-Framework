package newAgent.state;

import java.util.ArrayList;

public class DataGroup extends StateData {

	private static final int MAX_1 = 9;
	private static final int MAX_DISTANCE_DISCRETE = 9;
	
	public class Test1 extends Dimension<Integer> {

		public Test1(Integer rawValue) {
			super(rawValue, MAX_1, "Test");
		}

		@Override
		public Dimension<Integer> getNewDimension() {
			// TODO Auto-generated method stub
			return new Test1(1);
		}

		@Override
		protected int impDiscretize() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public Integer getCurrentValueFromBot() {
			// TODO Auto-generated method stub
			return 1;
		}
		
	}
	

	public static ArrayList<Integer> getNumValuesPerDims() {
		ArrayList<Integer> a = new ArrayList<>();

		a.add(MAX_1 + 1);

		return a;
	}

	
	public static int getNumDims() {
		return getNumValuesPerDims().size();
	}
	
	public DataGroup() {
		this.dimensions = new ArrayList<>();
		
		this.dimensions.add(new Test1(1));
	}
	
	@Override
	public StateData getNewStateData() {
		// TODO Auto-generated method stub
		DataGroup r = new DataGroup();
		
		r.dimensions.clear();

		for (Dimension<?> dimension : dimensions) {
			r.dimensions.add(dimension.getNewDimension());
		}
		
		return r;
	}

}
