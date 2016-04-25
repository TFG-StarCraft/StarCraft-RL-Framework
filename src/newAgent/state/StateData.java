package newAgent.state;

import java.util.ArrayList;

import com.Com;

public abstract class StateData {

	protected Com com;
	protected ArrayList<Dimension<?>> dimensions;

	public abstract StateData getNewStateData();

	public ArrayList<Dimension<?>> getValues() {
		return dimensions;
	}
}
