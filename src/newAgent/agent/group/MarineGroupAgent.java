package newAgent.agent.group;

import java.util.ArrayList;
import java.util.List;

import com.Com;

import bot.Bot;
import bot.action.GenericAction;
import bot.commonFunctions.CheckAround;
import bwapi.Unit;
import newAgent.agent.GenericAgent;
import newAgent.event.factories.AEFDestruirUnidad;
import newAgent.event.factories.AEFGroup;
import newAgent.master.GenericMaster;
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
	
	public MarineGroupAgent(GenericMaster master, Com com, Bot bot, List<Unit> units, int numGroup) {
		super(master, com, bot);
		this.allUnits = new ArrayList<>(units);
		this.aliveUnits = new ArrayList<>(units);
		this.deadUnits = new ArrayList<>();
		this.frameCount = 0;
		this.numGroup = numGroup;
		this.enemies = new ArrayList<>();
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumDims() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<Integer> getNumValuesPerDims() {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public boolean solveEventsAndCheckEnd() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getRewardUpdated() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Boolean getOnFinalUpdated() {
		// TODO Auto-generated method stub
		return null;
	}

}
