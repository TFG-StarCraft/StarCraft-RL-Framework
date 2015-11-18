package bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.Com;

import bot.UnitWrapper.BWAPI_UnitToWrapper;
import bot.UnitWrapper.UnitWrapper;
import bot.action.GenericAction;
import bot.observers.OnUnitDestroyObserver;
import bot.observers.unit.GenericUnitObserver;
import bwapi.DefaultBWListener;
import bot.event.Event;
import bwapi.Game;
import bwapi.Mirror;
import bwapi.Player;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;

public abstract class Bot extends DefaultBWListener implements Runnable {

	protected Com com;
	
	public Mirror mirror;
	public Game game;
	public Player self;

	private BWAPI_UnitToWrapper unitToWrapper;

	private boolean firstStart;
	private boolean firstExec;
	private boolean restarting;

	public boolean guiEnabled;
	public long frames;

	private ArrayList<OnUnitDestroyObserver> onUnitDestroyObs;
	protected HashMap<Integer, ArrayList<GenericUnitObserver>> genericObservers;

	protected ArrayList<Event> events;

	public Bot(Com com) {
		this.com = com;
		this.mirror = new Mirror();
		this.firstStart = true;
		this.guiEnabled = true;

		this.onUnitDestroyObs = new ArrayList<>();
		this.genericObservers = new HashMap<>();

		this.events = new ArrayList<>();
	}

	@Override
	public void onStart() {
		this.frames = 0;
		// onStart is also called after re-start
		this.game = mirror.getGame();
		this.self = game.self();
		this.game.setGUI(guiEnabled);
		//this.game.setLocalSpeed(0);

		this.firstExec = true;
		this.unitToWrapper = new BWAPI_UnitToWrapper();
		this.restarting = false;

		this.com.ComData.onFinal = false;
		this.com.ComData.restart = false;

		this.onUnitDestroyObs.clear();
		this.genericObservers.clear();
		this.events.clear();

		if (firstStart) { // Only enters the very first execution (restarts wont
							// enter here)
			// Use BWTA to analyze map
			// This may take a few minutes if the map is processed first time!
			com.onSendMessage("Analyzing map...");
			BWTA.readMap();
			BWTA.analyze();
			com.onSendMessage("Map data ready");
			this.firstStart = false;
		}

		this.com.Sync.s_restartSync.release();
	}

	@Override
	public void onUnitDestroy(Unit unit) {
		super.onUnitDestroy(unit);

		for (OnUnitDestroyObserver observer : onUnitDestroyObs) {
			observer.onUnitDestroy(unit);
		}
	}

	@Override
	public void onFrame() {
		this.frames++;
		if (shouldExecuteOnFrame()) {
			// Draw info even if paused (at the end)
			if (!game.isPaused()) {
				this.events.clear();
				ArrayList<GenericAction> actionsToRegister = com.ComData.actionQueue.getQueue();
				
				for (GenericAction action : actionsToRegister) {
					action.register();
				}
				
				if (this.firstExec) {
					firstExecOnFrame();
				}

				for (Unit rawUnit : self.getUnits()) {
					ArrayList<GenericUnitObserver> a = genericObservers.get(rawUnit.getID());
					if (a != null) {
						for (Iterator<GenericUnitObserver> it = a.iterator(); it.hasNext();) {
							GenericUnitObserver observer = (GenericUnitObserver) it.next();
							observer.onUnit(rawUnit);
						}
//						for (GenericUnitObserver observer : a) {
//							observer.onUnit(rawUnit);
//						}
					}
				}

				// End check

				// for (Unit unit : self.getUnits()) {
				// if (unit.getType().equals(UnitType.Terran_Command_Center)) {
				// com.ComData.enBaliza =
				// unit.distanceTo(com.ComData.unit.getUnit().getPosition()) <
				// 150;
				// }
				// }

				checkEnd();
				com.Sync.s_end.release();
			}
			printUnitsInfo();
		}
	}

	public abstract void checkEnd();

	// Called on the first execution of onFrame
	private void firstExecOnFrame() {
		for (Unit unit : self.getUnits()) {
			if (unit.getType().equals(UnitType.Terran_Marine)) {
				com.ComData.unit = new UnitWrapper(unit);
				unitToWrapper.put(com.ComData.unit);
				if (com.ComData.iniX == -1) {
					com.ComData.iniX = unit.getX();
					com.ComData.iniY = unit.getY();
				}
			}
		}
		com.ComData.onFinal = false;
		this.firstExec = false;
		com.Sync.s_initSync.release();
	}

	private boolean isRestarting() {
		if (!restarting) {
			if (com.ComData.restart) {
				com.onSendMessage("Restart2...");
				restarting = true;
				game.restartGame();
			}
		}

		return restarting;
	}

	private boolean shouldExecuteOnFrame() {
		return !game.isReplay() && !isRestarting();
	}

	private void printUnitsInfo() {
		for (Unit myUnit : self.getUnits()) {
			if (myUnit.getType().equals(UnitType.Terran_Marine)) {
				game.drawTextMap(myUnit.getPosition().getX(), myUnit.getPosition().getY(),
						"Order: " + myUnit.getOrder() + myUnit.getPosition().toString());
				game.drawLineMap(myUnit.getPosition().getX(), myUnit.getPosition().getY(),
						myUnit.getOrderTargetPosition().getX(), myUnit.getOrderTargetPosition().getY(),
						bwapi.Color.Red);
			}
		}
	}

	@Override
	public void run() {
		mirror.getModule().setEventListener(this);
		mirror.startGame();
	}

	public void registerOnUnitDestroyObserver(OnUnitDestroyObserver o) {
		this.onUnitDestroyObs.add(o);
	}

	public void unRegisterOnUnitDestroyObserver(OnUnitDestroyObserver obs) {
		this.onUnitDestroyObs.remove(obs);
	}
	
	public void registerOnUnitObserver(GenericUnitObserver obs) {
		
		if (genericObservers.containsKey(obs.getUnit().getID())) {
			ArrayList<GenericUnitObserver> a = genericObservers.get(obs.getUnit().getID());
			a.add(obs);

			genericObservers.put(obs.getUnit().getID(), a);
		} else {
			ArrayList<GenericUnitObserver> a = new ArrayList<>();
			a.add(obs);

			genericObservers.put(obs.getUnit().getID(), a);
		}

	}
	
	public void unRegisterOnUnitObserver(GenericUnitObserver obs) {
		
		if (genericObservers.containsKey(obs.getUnit().getID())) {
			ArrayList<GenericUnitObserver> a = genericObservers.get(obs.getUnit().getID());
			a.remove(obs);

			genericObservers.put(obs.getUnit().getID(), a);
		}
	}

	public void addEvent(Event event) {
		this.events.add(event);
	}

}
