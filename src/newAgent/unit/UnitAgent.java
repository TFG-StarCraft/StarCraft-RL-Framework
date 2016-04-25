package newAgent.unit;

import com.Com;

import bot.Bot;
import bwapi.Unit;
import newAgent.GenericAgent;

public abstract class UnitAgent extends GenericAgent {
		
	public UnitAgent(Unit unit, Com com, Bot bot) {
		super(unit, com, bot);
	}

	@Override
	public void onUnit(Unit unit) {
		// TODO Auto-generated method stub

	}

}
