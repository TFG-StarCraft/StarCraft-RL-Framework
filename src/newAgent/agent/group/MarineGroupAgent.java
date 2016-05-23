package newAgent.agent.group;

import java.util.ArrayList;
import java.util.List;

import com.Com;

import bot.Bot;
import bot.action.GenericAction;
import bot.commonFunctions.CheckAround;
import bwapi.Unit;
import newAgent.Const;
import newAgent.agent.GenericAgent;
import newAgent.decisionMaker.DM_LambdaQE;
import newAgent.decisionMaker.Shared_LambdaQE;
import newAgent.event.AbstractEvent;
import newAgent.event.factories.AEFGroup;
import newAgent.master.GenericMaster;
import newAgent.state.DataGroup;
import newAgent.state.State;

public class MarineGroupAgent extends GenericAgent {

	private static final int TIMEOUT = 2500;

	private List<Unit> allUnits;
	private List<Unit> aliveUnits;
	private List<Unit> deadUnits;
	private List<Unit> enemies;

	private double iniMyHP, iniEnemyHP, endMyHP, endEnemyHP;

	private int frameCount;
	private int numGroup;

	public MarineGroupAgent(GenericMaster master, Com com, Bot bot, List<Unit> units, int numGroup, Shared_LambdaQE shared) {
		super(master, com, bot);
		this.allUnits = new ArrayList<>(units);
		this.aliveUnits = new ArrayList<>(units);
		this.deadUnits = new ArrayList<>();
		this.frameCount = 0;
		this.numGroup = numGroup;
		this.enemies = new ArrayList<>();
		// TODO Auto-generated constructor stub
		

		this.decisionMaker = new DM_LambdaQE(this, shared);
	}

	@Override
	protected void setUpFactory() {
		this.factory = new AEFGroup(com);
	}

	///////////////////////////////////////////////////////////////////////////
	// ENVIRONMENT ////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	@Override
	public State getInitState() {
		this.waitForBotEndsInit();
		
		return new State(this, new DataGroup());
	}

	@Override
	public int getNumDims() {
		return DataGroup.getNumDims();
	}

	@Override
	public ArrayList<Integer> getNumValuesPerDims() {
		return DataGroup.getNumValuesPerDims();
	}

	///////////////////////////////////////////////////////////////////////////
	// BWAPI //////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	@Override
	public void onFirstFrame() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFrame() {
		// TODO Auto-generated method stub
		if (frameCount >= TIMEOUT) {
			master.onTimeOut();
			return;
		}

		frameCount++;
		ArrayList<GenericAction> actionsToRegister = this.actionsToDispatch.getQueueAndFlush();

		if (actionsToRegister.size() > 1) {
			System.err.println("More than 1 action to register");
		}

		for (GenericAction action : actionsToRegister) {
			onNewAction(action);
		}

		if (this.currentAction != null)
			this.currentAction.onFrame();

		for (Unit unit : aliveUnits) {
			for (Unit u : CheckAround.getEnemyUnitsAround(unit)) {
				if (!this.enemies.contains(u))
					this.enemies.add(u);
			}
		}
	}

	@Override
	public void onUnitDestroy(Unit u) {
		if (this.aliveUnits.contains(u)) {
			this.aliveUnits.remove(u);
			this.deadUnits.add(u);

			if (this.aliveUnits.size() == 0) {
				addEvent(factory.newAbstractEvent(AEFGroup.CODE_DEAD_ALL, (Integer) numGroup));
			} else {
				addEvent(factory.newAbstractEvent(AEFGroup.CODE_DEAD, (Integer) numGroup));
			}
		} else {
			if (this.enemies.contains(u)) {
				this.enemies.remove(u);
				if (this.enemies.size() == 0)
					addEvent(factory.newAbstractEvent(AEFGroup.CODE_KILL_ALL, (Integer) numGroup));
				else
					addEvent(factory.newAbstractEvent(AEFGroup.CODE_KILL, (Integer) numGroup));
			}
		}
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub

	}

	///////////////////////////////////////////////////////////////////////////
	// ACTIONS ////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	@Override
	protected void onNewAction() {
		iniMyHP = 0;
		for (Unit unit : aliveUnits) {
			iniMyHP += unit.getHitPoints();
		}

		iniEnemyHP = 0;
		for (Unit unit : enemies) {
			iniEnemyHP += unit.getHitPoints();
		}

	}

	@Override
	public void onEndAction(GenericAction genericAction, boolean correct) {
		// TODO ASSERT PRE DEBUG
		if (genericAction != currentAction)
			com.onError("end action != current action", true);

		addEvent(factory.newAbstractEvent(AEFGroup.CODE_DEFAULT_ACTION, genericAction, correct));

	}

	///////////////////////////////////////////////////////////////////////////
	// EVENTS /////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	private double nextReward;
	private boolean endCondition;

	@Override
	public boolean solveEventsAndCheckEnd() {
		// TODO Auto-generated method stub

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

					endMyHP = 0;
					for (Unit unit : aliveUnits) {
						endMyHP += unit.getHitPoints();
					}
					endMyHP = endMyHP == 0 ? -1 : endMyHP;

					endEnemyHP = 0;
					for (Unit unit : enemies) {
						endEnemyHP += unit.getHitPoints();
					}
					endEnemyHP = endEnemyHP == 0 ? -1 : endEnemyHP;

					if (endEnemyHP != -1 && iniEnemyHP == -1) {
						// Killed enemies that initially unit didn't see

						List<Unit> l = new ArrayList<>();
						for (Unit unit : aliveUnits) {
							for (Unit u : CheckAround.getEnemyUnitsAround(unit)) {
								if (!l.contains(u))
									l.add(u);
							}
						}

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

				this.endCondition = event.isFinalEvent();

				event.notifyEvent();
				this.currentAction = null;

				// Signal AFTER onEnd and reward are set
				this.signalActionEnded();

				break;
			}
		}

		this.events.clear();
		return isFinal;
	}

	@Override
	public Boolean getOnFinalUpdated() {
		return endCondition;
	}

	@Override
	public double getRewardUpdated() {
		return 2;
	}

	public List<Unit> getUnits() {
		return this.aliveUnits;
	}

}
