package qLearning.agent.state;

import java.util.ArrayList;

import com.Com;

public abstract class StateData {

	protected Com com;
	protected ArrayList<Dimension<?>> dimensions;

	public int getNumDims() {
		return dimensions.size();
	}

	public StateData getNewStateData() {
		DataRelative r = new DataRelative(com);

		r.dimensions.clear();

		for (Dimension<?> dimension : dimensions) {
			r.dimensions.add(dimension.getNewDimension());
		}
		
		return r;
	}

	public ArrayList<Dimension<?>> getValues() {
		return dimensions;
	}

}
