package qLearning.agent;

import com.Com;

import qLearning.Const;
import qLearning.enviroment.AbstractEnviroment;

public class State {
	private int x;
	private int y;

	private Double reward;

	private AbstractEnviroment enviroment;
	private Com com;

	private Boolean finalState;

	public State(int x, int y, AbstractEnviroment e, Com com, boolean initialState) {
		this.com = com;
		this.x = x;
		this.y = y;
		this.reward = Double.NaN;
		this.finalState = initialState ? false : null;

		this.enviroment = e;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean esAccionValida(Action a) {

		return a.toAction(com).isPossible();
	}

	public State executeAction(Action action) {

		com.ComData.actionQueue.queueAction(action);

		com.Sync.waitForActionEnds();

		State SS = new State(com.ComData.unit.getX(), com.ComData.unit.getY(), this.enviroment, this.com, false);
		// TODO
		SS.finalState = com.ComData.getOnFinalUpdated();
		SS.reward = SS.calculateReward();


		return SS;
	}

	private double calculateReward() {
		if (finalState) {
			return Const.RECOMPENSA_FINAL;
		} else if (com.ComData.lastActionOk) {
			return Const.RECOMPENSA_GENERAL;
		} else {
			return Const.RECOMPENSA_ERROR;
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

}
