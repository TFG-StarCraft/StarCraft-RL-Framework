package newAgent.event.factories;

import newAgent.event.AbstractEvent;

public abstract class AbstractEventsFactory {

	public abstract AbstractEvent newAbstractEvent(int code, Object... args);
	
}
