package bot;

import java.util.List;

import com.Com;

import bot.action.GenericAction;
import bot.commonFunctions.CheckAround;
import bot.commonFunctions.HP;
import bot.observers.UnitKilledObserver;
import bwapi.Unit;
import newAgent.event.AbstractEvent;
import newAgent.event.factories.AEFDestruirUnidad;
import newAgent.event.factories.AbstractEventsFactory;
import utils.DebugEnum;

public class BotDestruirUnidad extends Bot {

	public BotDestruirUnidad(Com com) {
		super(com);
	}

	@Override
	public AbstractEventsFactory getNewFactory() {
		return new AEFDestruirUnidad(com);
	}

	private double iniMyHP, iniEnemyHP, endMyHP, endEnemyHP;

	@Override
	public void onEndAction(GenericAction genericAction, Object... args) {
		addEvent(factory.newAbstractEvent(AEFDestruirUnidad.CODE_DEFAULT_ACTION, genericAction, args[0]));

		endMyHP = com.ComData.unit.getHitPoints();

		Unit unit = com.ComData.unit;

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
	}

	@Override
	public double getReward() {

		if (iniEnemyHP == -1 && endEnemyHP == -1) {
			return 0;
		}

		double r = (iniEnemyHP - endEnemyHP) / (double) iniEnemyHP - (iniMyHP - endMyHP) / (double) iniMyHP;
		return r * newAgent.Const.REWARD_MULT_FACTOR;
	}



	@Override
	public boolean solveEventsAndCheckEnd() {
		// TODO DEBUG assert pre
		if (com.ComData.getOnFinalUpdated())
			com.onError("Called solveEventsAndCheck while onFinal", true);
		
		/* Three possible scenarios:
		 * 1 - No events (and therefore no end)
		 * 2 - One event, kill event or actionEnd event, endEvent is determined by the event itself
		 * 3 - Two events, caused when the unit ends moving and kills the target in the same frame. This causes an end.
		 */
		boolean isFinal = false;
		// Descending order (attend first more prio. events)
		java.util.Collections.sort(events, AbstractEvent.getPrioCompDescend());	
		
//		if (!events.isEmpty()) {
//			AbstractEvent event = events.get(0);
//			
//			isFinal = isFinal | event.isFinalEvent();
//			// on final is set *** BEFORE *** calling solveEvent, therefore is
//			// set BEFORE returning the control to Agent
//			com.ComData.setOnFinal(isFinal);
//			event.solveEvent();
//		}
		for (AbstractEvent event : events) {
			isFinal = isFinal | event.isFinalEvent();
			// on final is set *** BEFORE *** calling solveEvent, therefore is
			// set BEFORE returning the control to Agent

			com.ComData.setOnFinal(isFinal);
			event.solveEvent();

			if (event.returnsControlToAgent()) {
				break;
			}
		}

		return isFinal;
	}


}
