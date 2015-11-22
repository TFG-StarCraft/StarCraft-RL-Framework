package qLearning.agent;

import com.Com;

import qLearning.Const;
import qLearning.enviroment.AbstractEnviroment;

public class State {
	private int x;
	private int y;

	private Double reward;
	
	private boolean enFinal;

	private AbstractEnviroment enviroment;
	private Com com;

	public State(int x, int y, AbstractEnviroment e, Com com) {
		this.com = com;
		this.x = x;
		this.y = y;
		this.enFinal = false;
		this.reward = Double.NaN;

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

		com.ComData.actionQueue.put(action);

		com.Sync.waitForActionEnds();

		State SS = new State(com.ComData.unit.getUnit().getX(), com.ComData.unit.getUnit().getY(), this.enviroment,
				this.com);
		SS.enFinal = com.ComData.onFinal;
		SS.reward = SS.calculateReward();

		return SS;
	}

	public boolean isEnd() {
		return enFinal;
	}

	private double calculateReward() {
		if (isEnd()) {
			return Const.RECOMPENSA_FINAL;
		} else if (com.ComData.lastActionOk) {
			return Const.RECOMPENSA_GENERAL;
		} else {
			return Const.RECOMPENSA_ERROR;
		}
	}

	public boolean isFinalEnd() {
		System.out.println("Check end");
		com.Sync.waitForEndOfIterationCanBeChecked();
		System.out.println("Checked");
		
		return enFinal;
	}

	public Double getReward() {
		if (this.reward.isNaN())
			throw new RuntimeException("Reward is NaN");
		
		return this.reward;
	}

}
