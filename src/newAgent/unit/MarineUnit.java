package newAgent.unit;

import java.util.ArrayList;
import java.util.List;

import com.Com;

import bot.Bot;
import bot.action.GenericAction;
import bot.commonFunctions.CheckAround;
import bot.commonFunctions.HP;
import bwapi.Unit;
import newAgent.Const;
import newAgent.decisionMaker.DM_LambdaQE;
import newAgent.decisionMaker.DecisionMakerPrams;
import newAgent.event.AbstractEvent;
import newAgent.event.factories.AEFDestruirUnidad;
import newAgent.state.DataMarine;
import newAgent.state.State;

public class MarineUnit extends UnitAgent {

	public MarineUnit(Unit unit, Com com, Bot bot, DecisionMakerPrams params) {
		super(unit, com, bot);
		// TODO
		this.decisionMaker = new DM_LambdaQE(this, params);
	}

	@Override
	protected void setUpFactory() {
		this.factory = new AEFDestruirUnidad(com);
	}

	@Override
	public State getInitState() {
		// TODO d sync
		// com.Sync.waitForBotEndsInit();
		
		this.waitForBotEndsInit();
		
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

	// TODO cont
	int cont = 0;

	@Override
	public void onUnitKilled(Unit unit) {
		if (unit.getID() == (unit.getID())) {
			addEvent(factory.newAbstractEvent(AEFDestruirUnidad.CODE_KILLED));
			cont = 0;
		} else {
			// TODO solo unidad deseada
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
//		if (genericAction != currentAction)
//			com.onError("end action != current action", true);

		addEvent(factory.newAbstractEvent(AEFDestruirUnidad.CODE_DEFAULT_ACTION, genericAction, correct));
	}

	@Override
	public void onFrame() {
		// TODO Auto-generated method stub
		/*
		 * ArrayList<GenericAction> actionsToRegister =
		 * com.ComData.actionQueue.getQueueAndFlush();
		 * 
		 * 
		 * 
		 * for (GenericAction action : actionsToRegister) {
		 * action.registerOnUnitObserver(); onNewAction(action, (Object[])
		 * null); }
		 */
		ArrayList<GenericAction> actionsToRegister = this.actionsToDispatch.getQueueAndFlush();

		for (GenericAction action : actionsToRegister) {
			action.registerOnUnitObserver();
			onNewAction(action);
		}
	}

	private boolean lastEndCondition;

	@Override
	public void notifyEnd(boolean tmp) {
		lastEndCondition = tmp;
		// TODO d Notify
		signalEndCanBeChecked();
	}

	@Override
	public Boolean getOnFinalUpdated() {
		// TODO d wait
		waitForEndCanBeChecked();
		return lastEndCondition;
	}
	
	private double nextReward;

	@Override
	public double getRewardUpdated() {
		return nextReward;
	}

	@Override
	public boolean solveEventsAndCheckEnd() {
		// Three possible scenarios:
		// 1 - No events (and therefore no end)
		// 2 - One event, kill event or actionEnd event, endEvent is determined
		// by the event itself
		// 3 - Two events, caused when the unit ends moving and kills the target
		// in the same frame. This causes an end.

		boolean isFinal = false;
		// Descending order (attend first more prio. events)
		java.util.Collections.sort(events, AbstractEvent.getPrioCompDescend());

		for (AbstractEvent event : events) {
			isFinal = isFinal | event.isFinalEvent();
			if (event.returnsControlToAgent()) {
				// Calculate reward for current ending action
				if (event.isFinalEvent()) {
					nextReward = event.isGoalState() ? Const.REWARD_SUCCESS : Const.REWARD_FAIL;
				} else {
					// Calculate end hp's
					endMyHP = unit.getHitPoints();
					endEnemyHP = HP.getHPOfEnemiesAround(unit);
					if (endEnemyHP != -1 && iniEnemyHP == -1) {
						// Killed enemies that initially unit didn't see
						List<Unit> l = CheckAround.getEnemiesAround(unit);
						if (l.isEmpty())
							iniEnemyHP = -1;

						iniEnemyHP = 0.0;
						for (int i = 0; i < l.size(); i++) {
							iniEnemyHP += l.get(i).getHitPoints();
						}
					}

					if (iniEnemyHP == -1 && endEnemyHP == -1) {
						nextReward = 0;
					}

					double r = (iniEnemyHP - endEnemyHP) / (double) iniEnemyHP - (iniMyHP - endMyHP) / (double) iniMyHP;
					nextReward = r * newAgent.Const.REWARD_MULT_FACTOR;
				}

				event.notifyEvent();
				// Signal AFTER onEnd and reward are set
				this.signalActionEnded();
				this.currentAction.unRegisterOnUnitObserver();
				
				break;
			}
		}

		return isFinal;
	}

	///////////////////////////////////////////////////////////////////////////
	// SYNC ///////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

}