package newAgent.agent.group;

import java.util.ArrayList;
import java.util.List;

import com.Com;

import bot.Bot;
import bot.action.GenericAction;
import bwapi.Unit;
import newAgent.agent.GenericAgent;
import newAgent.event.factories.AEFGroup;
import newAgent.master.GenericMaster;
import newAgent.state.State;

public class MarineGroupAgent extends GenericAgent {
	
	private List<Unit> units;

	public MarineGroupAgent(GenericMaster master, Com com, Bot bot, List<Unit> units) {
		super(master, com, bot);
		this.units = units;
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
		
	}

	@Override
	public void onUnitDestroy(Unit u) {
		if (this.units.contains(u)) {
			
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEndAction(GenericAction genericAction, boolean correct) {
		// TODO Auto-generated method stub
		
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
