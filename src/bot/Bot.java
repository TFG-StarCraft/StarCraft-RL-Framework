package bot;

import java.util.ArrayList;
import java.util.HashMap;

import com.Com;

import bot.UnitWrapper.BWAPI_UnitToWrapper;
import bot.UnitWrapper.UnitWrapper;
import bot.action.GenericAction;
import bot.observers.UnitDestroyObserver;
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
	private boolean end;
	
	public boolean guiEnabled;
	public long frames;
	public int frameSpeed;
	private int lastFrameSpeed;

	private ArrayList<UnitDestroyObserver> onUnitDestroyObs;
	protected HashMap<Integer, ArrayList<GenericUnitObserver>> genericObservers;

	protected ArrayList<Event> events;

	private ArrayList<UnitDestroyObserver> unitDestroyQueue;

	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	public abstract boolean checkEnd();

	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	public Bot(Com com) {
		this.com = com;
		this.mirror = new Mirror();
		this.firstStart = true;
		this.guiEnabled = true;

		this.frameSpeed = 0;
		this.lastFrameSpeed = frameSpeed;
		
		this.onUnitDestroyObs = new ArrayList<>();
		this.genericObservers = new HashMap<>();

		this.events = new ArrayList<>();

		this.unitDestroyQueue = new ArrayList<>();
	}

	///////////////////////////////////////////////////////////////////////////
	// BWAPI CALLS ////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	@Override
	public void onStart() {
		// onStart is also called after re-start
		this.game = mirror.getGame();
		this.self = game.self();
		this.game.setGUI(guiEnabled);
		this.game.setLocalSpeed(frameSpeed);

		this.frames = 0;
		this.firstExec = true;
		this.unitToWrapper = new BWAPI_UnitToWrapper();
		this.restarting = false;

		this.com.ComData.onFinal = false;
		this.com.ComData.restart = false;
		this.end = false;
		
		this.onUnitDestroyObs.clear();
		this.genericObservers.clear();
		this.events.clear();

		this.unitDestroyQueue.clear();

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

		this.com.Sync.signalGameIsReady();
	}

	@Override
	public void onUnitDestroy(Unit unit) {
		com.onDebugMessage("DESTROY " + frames);
		/*
		 * int i = 0; while (i < onUnitDestroyObs.size()) { int lastSize =
		 * onUnitDestroyObs.size(); UnitDestroyObserver observer =
		 * onUnitDestroyObs.get(i); observer.onUnitDestroy(unit);
		 * 
		 * if (lastSize == onUnitDestroyObs.size()) i++; // else en onUnit se
		 * llamó a unRegister y se eliminó ese observador }
		 */

		if (unit.exists() && unit.getType().equals(UnitType.Terran_Marine)) {
			com.bot.addEvent(new Event(Event.CODE_KILLED));
		} else {
			com.bot.addEvent(new Event(Event.CODE_KILL));
		}

		super.onUnitDestroy(unit);
	}
	
	@Override
	public void onFrame() {
		com.onDebugMessage("Frame " + this.frames + " Units " + this.game.getAllUnits().size());
		if (shouldExecuteOnFrame()) {
			// Draw info even if paused (at the end)
			if (!end && !game.isPaused()) {
				if (this.firstExec) {
					firstExecOnFrame();
					com.Sync.signalInitIsDone();
				} else {
					end = checkEnd();
					// for (UnitDestroyObserver observer : unitDestroyQueue) {
					// this.onUnitDestroyObs.remove(observer);
					// }
					events.clear();
					// unitDestroyQueue.clear();
					// This signals that the PREVIOUS onFrame was executed
					com.Sync.signalIsEndCanBeChecked();
				}

				this.frames++;
				showFramesPerSecs();

				if (!end) {
					if (lastFrameSpeed != frameSpeed) {
						lastFrameSpeed = frameSpeed;
						this.game.setLocalSpeed(frameSpeed);
					}
					
					ArrayList<GenericAction> actionsToRegister = com.ComData.actionQueue.getQueue();

					for (GenericAction action : actionsToRegister) {
						action.registerUnitObserver();
					}

					for (Unit rawUnit : self.getUnits()) {
						ArrayList<GenericUnitObserver> a = genericObservers.get(rawUnit.getID());
						if (a != null) {
							int i = 0;
							while (i < a.size()) {
								int lastSize = a.size();
								GenericUnitObserver observer = a.get(i);
								observer.onUnit(rawUnit);

								if (lastSize == a.size())
									i++;
								// else en onUnit se llamó a unRegister y se
								// eliminó
								// ese observador
							}
						}
					}

					// End check

					// for (Unit unit : self.getUnits()) {
					// if
					// (unit.getType().equals(UnitType.Terran_Command_Center)) {
					// com.ComData.enBaliza =
					// unit.distanceTo(com.ComData.unit.getUnit().getPosition())
					// <
					// 150;
					// }
					// }
				}
			}
			printUnitsInfo();
		}
	}

	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	// Called on the first execution of onFrame
	private void firstExecOnFrame() {
		for (Unit unit : self.getUnits()) {
			if (unit.getType().equals(UnitType.Terran_Marine)) {
				com.ComData.unit = new UnitWrapper(unit);
				unitToWrapper.put(com.ComData.unit);

				com.ComData.iniX = unit.getX();
				com.ComData.iniY = unit.getY();
			}
		}
		com.ComData.onFinal = false;
		this.firstExec = false;
	}

	private boolean isRestarting() {
		if (!restarting) {
			if (com.ComData.restart) {
				com.onSendMessage("Restart bot call...");
				restarting = true;
				game.restartGame();
			}
		}

		return restarting;
	}

	private boolean shouldExecuteOnFrame() {
		return !game.isReplay() && !isRestarting();
	}

	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	public void registerOnUnitDestroyObserver(UnitDestroyObserver o) {
		this.onUnitDestroyObs.add(o);
	}

	public void unRegisterOnUnitDestroyObserver(UnitDestroyObserver obs) {
		// this.unitDestroyQueue.add(obs);

		// this.onUnitDestroyObs.remove(obs);
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
		com.onDebugMessage("EVENT " + frames);
		this.events.add(event);
	}

	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	@Override
	public void run() {
		mirror.getModule().setEventListener(this);
		mirror.startGame();
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

	private long lastTime = System.currentTimeMillis();
	private long lastFrames = -1;

	private void showFramesPerSecs() {
		if (System.currentTimeMillis() - lastTime > 100) {
			lastTime = System.currentTimeMillis();

			com.onFpsAverageAnnouncement((frames - lastFrames) / 0.1);

			lastFrames = frames;
		}
	}

}
