package qLearning.enviroment;

import com.Com;

import bwapi.UnitType;
import qLearning.Const;
import qLearning.agent.State;

public class SCEnviroment implements AbstractEnviroment {

	private int sizeX;
	private int sizeY;

	private Com com;

	public SCEnviroment(Com com) {

		this.sizeX = Const.TAM_X;
		this.sizeY = Const.TAM_Y;

		this.com = com;

	}

	@Override
	public int getSizeX() {
		return sizeX;
	}

	@Override
	public int getSizeY() {
		return sizeY;
	}

	@Override
	public State getInitState() {
		com.Sync.waitForBotEndsInit();
		
		return new State(9, 9, this, com, true);
	}

}
