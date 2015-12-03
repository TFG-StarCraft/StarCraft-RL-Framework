package bot.UnitWrapper;

import bot.action.GenericAction;

public class ActionDispatcher {

	// TODO ARRAY?
	private GenericAction action;

	public ActionDispatcher() {

	}

	public void checkAndDispatchAll() {
		if (action != null)
			action.executeAction();
	}

	public void addAction(GenericAction action) {
		if (this.action == null)
			this.action = action;
	}

	public void remove() {
		this.action = null;
	}

}
