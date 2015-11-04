package bot.UnitWrapper;

import bot.action.GenericAction;
import bwapi.Unit;

public class UnitWrapper {

	private final Unit unit;
	private final ActionDispatcher dispatcher;
	
	public UnitWrapper(Unit unit) {
		this.unit = unit;
		this.dispatcher = new ActionDispatcher();
	}

	public void checkAndDispatchActions() {
		this.dispatcher.checkAndDispatchAll();
	}

	public int getId() {
		return this.unit.getID();
	}
	
	public Unit getUnit(){
		return this.unit;
	}
	
	public void addAction(GenericAction action) {
		this.dispatcher.addAction(action);
	}

	public void removeAction() {
		this.dispatcher.remove();
	}
	
}
