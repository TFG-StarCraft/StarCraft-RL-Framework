package bot;

import java.util.ArrayList;
import java.util.HashMap;

import com.Com;

import bot.action.GenericAction;
import bot.observers.GenericUnitObserver;
import bwapi.DefaultBWListener;
import bot.event.AbstractEvent;
import bot.event.factories.AbstractEventsFactory;
import bwapi.Game;
import bwapi.Mirror;
import bwapi.Player;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import utils.DebugEnum;

public abstract class Bot extends DefaultBWListener implements Runnable {

	protected Com com;

	public Mirror mirror;
	public Game game;
	public Player self;

	private boolean firstOnStart;
	private boolean firstOnFrame;
	private boolean restarting;
	private boolean endConditionSatisfied;

	public boolean guiEnabled;
	public long frames;
	public int frameSpeed;
	private int lastFrameSpeed;

	// Maps units (with its unique id) to an arrayList of observers
	protected HashMap<Integer, ArrayList<GenericUnitObserver>> genericObservers;

	protected ArrayList<AbstractEvent> events;

	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	public abstract boolean solveEventsAndCheckEnd();
	
	protected final AbstractEventsFactory factory;
	public abstract AbstractEventsFactory getNewFactory();

	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	public Bot(Com com) {
		this.com = com;
		this.mirror = new Mirror();
		this.firstOnStart = true;
		this.guiEnabled = true;

		this.frameSpeed = 0;
		this.lastFrameSpeed = frameSpeed;

		this.genericObservers = new HashMap<>();

		this.events = new ArrayList<>();
		this.factory = getNewFactory();
	}

	///////////////////////////////////////////////////////////////////////////
	// BWAPI CALLS ////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	public int epoch = -1;
	
	@Override
	public void onStart() {
		// onStart is also called after re-start
		this.epoch++;
		this.game = mirror.getGame();
		this.self = game.self();
		this.game.setGUI(guiEnabled);
		this.game.setLocalSpeed(frameSpeed);

		this.frames = 0;
		this.firstOnFrame = true;
		this.restarting = false;
		this.endConditionSatisfied = false;

		this.com.ComData.resetFinal();
		this.com.ComData.restart = false;

		this.genericObservers.clear();
		this.events.clear();
		com.ComData.actionQueue.clear();

		if (firstOnStart) { // Only enters the very first execution (restarts
							// wont enter here)
			// Use BWTA to analyze map
			// This may take a few minutes if the map is processed first time!
			com.onSendMessage("Analyzing map...");
			BWTA.readMap();
			BWTA.analyze();
			com.onSendMessage("Map data ready");
			this.firstOnStart = false;
		}

		this.com.Sync.signalGameIsReady();
	}

	@Override
	public void onFrame() {
		com.onDebugMessage("Frame " + this.frames + " Units " + this.game.getAllUnits().size(), DebugEnum.FRAMES);
		if (shouldExecuteOnFrame()) {
			// Draw info even if paused (at the end)
			if (!endConditionSatisfied && !game.isPaused()) {
				if (this.firstOnFrame) {
					firstExecOnFrame();
					com.Sync.signalInitIsDone();
				} else {
					endConditionSatisfied = solveEventsAndCheckEnd();
					events.clear();
					com.ComData.setOnFinal(endConditionSatisfied);
					
					// This signals that the PREVIOUS onFrame was executed
					//com.Sync.signalIsEndCanBeChecked();
				}

				this.frames++;
				showFramesPerSecs();
				updateGameSpeed();

				if (!endConditionSatisfied) {
					ArrayList<GenericAction> actionsToRegister = com.ComData.actionQueue.getQueueAndFlush();

					for (GenericAction action : actionsToRegister) {
						action.registerUnitObserver();
					}

					for (Unit rawUnit : self.getUnits()) {
						ArrayList<GenericUnitObserver> a = genericObservers.get(rawUnit.getID());
						
						if (a != null) {
							if (a.size() > 1) {
								com.onSendMessage("NumOfActions: " + a.size());			
							}
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
				com.ComData.unit = unit;

				com.ComData.iniX = unit.getX();
				com.ComData.iniY = unit.getY();
			}
		}
		com.ComData.resetFinal();
		this.firstOnFrame = false;
	}

	/**
	 * Checks if the com.ComData.restar flag is true; and if so restarts the
	 * game
	 * 
	 * @return true if the game is restarting or intended to be
	 */
	private boolean isRestarting() {
		if (!restarting) {
			if (com.ComData.restart) {
				com.onSendMessage("Restart bot call...");
				this.restarting = true;
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

	public void addEvent(AbstractEvent event) {
		com.onDebugMessage("EVENT " + frames, DebugEnum.EVENT_AT_FRAME);
		this.events.add(event);
	}
	
	public abstract void onEndAction(GenericAction genericAction, Object...args);

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
		if (System.currentTimeMillis() - lastTime > 50) {
			lastTime = System.currentTimeMillis();

			com.onFpsAverageAnnouncement((frames - lastFrames) / 0.05);

			lastFrames = frames;
		}
	}

	/**
	 * Updates the current game speed so its running at the desired speed
	 * (as set in the gui)
	 */
	private void updateGameSpeed() {
		if (lastFrameSpeed != frameSpeed) {
			lastFrameSpeed = frameSpeed;
			this.game.setLocalSpeed(frameSpeed);
		}
	}
}
