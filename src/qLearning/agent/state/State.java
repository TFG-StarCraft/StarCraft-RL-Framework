package qLearning.agent.state;

import java.util.ArrayList;

import com.Com;

import qLearning.Const;
import qLearning.agent.Action;
import qLearning.enviroment.AbstractEnviroment;

public class State {

	private StateData data;

	private Double reward;

	private AbstractEnviroment enviroment;
	private Com com;

	private Boolean finalState;

	public static State newInitialState(AbstractEnviroment e, Com com) {
		return new State(new DataRelative(com), e, com, true);
	}
	
	private State(StateData data, AbstractEnviroment e, Com com, boolean initialState) {
		this.com = com;
		this.data = data;
		this.reward = Double.NaN;
		this.finalState = initialState ? false : null;

		this.enviroment = e;
	}

	public StateData getData() {
		return data;
	}

	public boolean esAccionValida(Action a) {

		return a.toAction(com).isPossible();
	}

	public State executeAction(Action action) {

		com.ComData.actionQueue.queueAction(action);

		com.Sync.waitForActionEnds();

		State SS = new State(this.data.getNewStateData(), this.enviroment, this.com, false);

		SS.finalState = com.ComData.getOnFinalUpdated();
		SS.reward = SS.calculateReward();
		
		return SS;
	}

	private double calculateReward() {
		if (finalState) {
			if (com.ComData.isFinalStateGoal)
				return Const.REWARD_SUCCESS;
			else {
				return Const.REWARD_FAIL;
			}
		} else {
			return com.bot.getReward();
		}
	}

	public boolean isFinalEnd() {
		return finalState;
	}

	public Double getReward() {
		if (this.reward.isNaN())
			throw new RuntimeException("Reward is NaN");

		return this.reward;
	}

	public int hashCode() {
		// TODO Correct?
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
