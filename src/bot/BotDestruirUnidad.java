package bot;

import java.util.Collection;

import com.Com;
import com.ComData;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import bot.action.GenericAction;
import bot.event.AbstractEvent;
import bot.event.factories.AbstractEventsFactory;
import bot.event.factories.AEFDestruirUnidad;
import bwapi.Unit;
import bwapi.UnitType;

public class BotDestruirUnidad extends Bot {

	public BotDestruirUnidad(Com com) {
		super(com);
	}

	@Override
	public AbstractEventsFactory getNewFactory() {
		return new AEFDestruirUnidad(com);
	}

	@Override
	public void onEndAction(GenericAction genericAction, Object... args) {
		addEvent(factory.newAbstractEvent(AEFDestruirUnidad.CODE_DEFAULT_ACTION, genericAction, args[0]));
	}

	@Override
	public void onUnitDestroy(Unit unit) {
		com.onDebugMessage("DESTROY " + frames);

		if (unit.exists() && unit.getType().equals(UnitType.Terran_Marine)) {
			addEvent(factory.newAbstractEvent(AEFDestruirUnidad.CODE_KILLED));
		} else {
			addEvent(factory.newAbstractEvent(AEFDestruirUnidad.CODE_KILL));
		}

		super.onUnitDestroy(unit);
	}

	@Override
	public boolean solveEventsAndCheckEnd() {
		// TODO DEBUG assert pre
		if (com.ComData.getOnFinalUpdated())
			throw new AssertionError("Called solveEventsAndCheck while onFinal");
		if (events.size() > 2)	
			throw new AssertionError("More than 2 events");
		
		
		/* Three possible scenarios:
		 * 1 - No events (and therefore no end)
		 * 2 - One event, kill event or actionEnd event, endEvent is determined by the event itself
		 * 3 - Two events, caused when the unit ends moving and kills the target in the same frame. This causes an end.
		 */
		boolean isFinal = false;
		// Descending order (attend first more prio. events)
		java.util.Collections.sort(events, AbstractEvent.getPrioCompDescend());	
		
		if (!events.isEmpty()) {
			AbstractEvent event = events.get(0);
			
			// TODO DEBUG ASSERT CODE
			if (events.size() == 2 && event.getCode() == AEFDestruirUnidad.CODE_DEFAULT_ACTION)	
				throw new AssertionError("2 events of default action");
			
			isFinal = isFinal | event.isFinalEvent();
			// on final is set *** BEFORE *** calling solveEvent, therefore is
			// set BEFORE returning the control to Agent
			com.ComData.setOnFinal(isFinal);
			event.solveEvent();
		}
//		for (AbstractEvent event : events) {
//			isFinal = isFinal | event.isFinalEvent();
//			// on final is set *** BEFORE *** calling solveEvent, therefore is
//			// set BEFORE returning the control to Agent
//			com.ComData.setOnFinal(isFinal);
//			event.solveEvent();
//		}

		return isFinal;
	}

}
