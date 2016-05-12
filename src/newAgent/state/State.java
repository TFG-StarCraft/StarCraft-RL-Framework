package newAgent.state;

import java.util.ArrayList;

import com.Com;

import newAgent.Action;
import newAgent.GenericAgent;

public class State {

	private StateData data;

	private Double reward;

	private Com com;

	private Boolean finalState;

	private GenericAgent agent;

	/**
	 * Initial state constructor
	 * 
	 * @param data
	 * @param com
	 */
	public State(GenericAgent agent, StateData data) {
		this(agent, data, true);
	}

	private State(GenericAgent agent, StateData data, boolean initialState) {
		this.agent = agent;
		this.data = data;
		this.com = agent.getCom();
		this.reward = Double.NaN;
		this.finalState = initialState ? false : null;
	}

	public StateData getData() {
		return data;
	}

	public boolean esAccionValida(Action a) {
		return a.toAction(com).isPossible();
	}

	public State executeAction(Action action) {
		agent.enqueueAction(action);
		agent.waitForActionEnds();

		State SS = new State(agent, this.data.getNewStateData(), false);

		SS.finalState = agent.getOnFinalUpdated();
		SS.reward = agent.getRewardUpdated();

		return SS;
	}

	public boolean isFinalState() {
		return finalState;
	}

	public Double getReward() {
		if (this.reward.isNaN())
			throw new RuntimeException("Reward is NaN");

		return this.reward;
	}

	public int hashCode() {
		long r = 0;

		ArrayList<Dimension<?>> a = data.getValues();
		int desp = 1;

		for (Dimension<?> dimension : a) {
			r += dimension.discretize() * desp;
			desp *= dimension.getNumOfValues();
		}

		if (r > Integer.MAX_VALUE)
			System.err.println("Warning, more than 2^32");

		return Long.hashCode(r);
	}

	@Override
	public boolean equals(Object obj) {
		return obj.getClass().equals(State.class) && data.getValues().equals(((State) obj).data.getValues());
	}

	@Override
	public String toString() {
		String s = "";
		for (Dimension<?> e : data.getValues()) {
			s += (e.getName() + ": " + e.discretize() + "\n");
		}
		return s;
	}
}
