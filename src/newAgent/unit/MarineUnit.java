package newAgent.unit;

import java.util.ArrayList;

import com.Com;

import bot.Bot;
import bot.action.GenericAction;
import bot.commonFunctions.HP;
import bwapi.Unit;
import newAgent.decisionMaker.DM_LambdaQE;
import newAgent.decisionMaker.GenericDecisionMaker;
import newAgent.event.factories.AEFDestruirUnidad;
import newAgent.state.DataMarine;
import newAgent.state.State;
import qLearning.Const;

public class MarineUnit extends UnitAgent {

	public MarineUnit(Unit unit, Com com, Bot bot) {
		super(unit, com, bot);
		this.decisionMaker = new DM_LambdaQE(this, );
	}
	
	@Override
	protected void setUpFactory() {
		this.factory = new AEFDestruirUnidad(com);
	}

	@Override
	public State getInitState() {
		// TODO sync
		// com.Sync.waitForBotEndsInit();
		return new State(this, new DataMarine(com, unit));
	}

	@Override
	public int getNumDims() {
		return DataMarine.getNumDims();
	}

	@Override
	public ArrayList<Integer> getNumValuesPerDims() {
		return DataMarine.getNumValuesPerDims();
	}

	@Override
	public double getReward(State state) {
		if (state.isFinalState()) {
			if (com.ComData.isFinalStateGoal)
				return Const.REWARD_SUCCESS;
			else {
				return Const.REWARD_FAIL;
			}
		} else {
			// TODO (botDestruirUnidad)
		}
	}
	
	// TODO cont
	int cont = 0;

	@Override
	public void onUnitKilled(Unit unit) {
		if (unit.getID() == (unit.getID())) {
			addEvent(factory.newAbstractEvent(AEFDestruirUnidad.CODE_KILLED));
			cont = 0;
		} else {
			if (cont == 0)
				cont++;
			else {
				addEvent(factory.newAbstractEvent(AEFDestruirUnidad.CODE_KILL));
				cont = 0;
			}
		}
	}

	private double iniMyHP, iniEnemyHP, endMyHP, endEnemyHP;
	
	@Override
	public void onNewAction() {
		iniMyHP = unit.getHitPoints();
		iniEnemyHP = HP.getHPOfEnemiesAround(unit);
	}

	@Override
	public void onEndAction(GenericAction genericAction, boolean correct) {
		// TODO ASSERT PRE DEBUG
		if (genericAction != currentAction)
			com.onError("end action != current action", true);
		
		
	}

}
