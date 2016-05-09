package newAgent.unit;

import com.Com;

import bot.Bot;
import bwapi.Unit;
import newAgent.GenericAgent;
import newAgent.Master;

public abstract class UnitAgent extends GenericAgent {
		
	public UnitAgent(Master master, Unit unit, Com com, Bot bot) {
		super(master, unit, com, bot);
	}

	@Override
	public void onUnit(Unit unit) {
		currentAction.onUnit(unit);
	}

}
