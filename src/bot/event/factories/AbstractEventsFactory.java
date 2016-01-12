package bot.event.factories;

import bot.event.AbstractEvent;

public abstract class AbstractEventsFactory {

	public abstract AbstractEvent newAbstractEvent(int code, Object... args);
	
}
