package qLearning.agent.estados;

import com.Com;

import qLearning.Const;
import qLearning.agent.Action;
import qLearning.enviroment.AbstractGridEnviroment;

public class State {
	private int x;
	private int y;

	private boolean isStart;
	private boolean baliza;

	private StateContainer states;
	private AbstractGridEnviroment enviroment;
	private Com com;

	State(int x, int y, StateContainer estados, AbstractGridEnviroment e, Com com) {
		this.com = com; 
		this.x = x;
		this.y = y;
		this.baliza = false;

		this.states = estados;
		this.enviroment = e;

		this.isStart = e.isStart(x, y);

	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean getBaliza() {
		return this.baliza;
	}

	public boolean esAccionValida(Action a) {

		return a.toAction(com).isPossible();
	}

	public State move(Action action) {

		com.ComData.action = action.toAction(com);
		try {
			com.Sync.s_postAction.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		State SS = new State(com.ComData.unit.getUnit().getX(), com.ComData.unit.getUnit().getY(), this.states,
				this.enviroment, this.com);
		SS.baliza = com.ComData.enBaliza;

		return SS;
	}

	public boolean isStart() {
		return isStart;
	}

	public boolean isEnd() {
		return baliza;
	}

	public double getReward() {
		if (isEnd()) {
			return Const.RECOMPENSA_FINAL;
		} else if (com.ComData.lastActionOk) {
			return Const.RECOMPENSA_GENERAL;
		} else {
			return Const.RECOMPENSA_ERROR;
		}
	}

}
