package qLearning.agent.state;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
		r.dimensions.addAll((this.dimensions.stream().map(e -> e.getNewDimension())).collect(Collectors.toList()));

		return r;
	}

	public ArrayList<Integer> getNumValuesPerDims() {
		List<Integer> l = (this.dimensions.stream().map(e -> e.getNumOfValues())).collect(Collectors.toList());

		return new ArrayList<Integer>(l);
	}

	public ArrayList<Dimension<?>> getValues() {
		return dimensions;
	}

}
