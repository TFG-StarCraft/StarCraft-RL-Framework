package bot;

import java.util.ArrayList;
import java.util.HashMap;

import com.Com;

import bot.action.GenericAction;
import bot.observers.OnUnitObserver;
import bot.observers.UnitKilledObserver;
import bwapi.Color;
import bwapi.DefaultBWListener;
import bwapi.Game;
import bwapi.Mirror;
import bwapi.Player;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import newAgent.event.AbstractEvent;
import newAgent.event.factories.AbstractEventsFactory;
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
	protected HashMap<Integer, ArrayList<OnUnitObserver>> genericObservers;
	protected ArrayList<UnitKilledObserver> unitKilledObservers;

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
		this.unitKilledObservers = new ArrayList<>();

		this.factory = getNewFactory();
	}

	///////////////////////////////////////////////////////////////////////////
	// BWAPI CALLS ////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	@Override
	public void onStart() {
		try {
			// onStart is also called after re-start
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
			this.unitKilledObservers.clear();

			this.events.clear();
			com.ComData.actionQueue.clear();

			if (firstOnStart) { // Only enters the very first execution
								// (restarts wont enter here)
				// Use BWTA to analyze map
				// This may take a few minutes if the map is processed first
				// time!
				com.onSendMessage("Analyzing map...");
				BWTA.readMap();
				BWTA.analyze();
				com.onSendMessage("Map data ready");
				this.firstOnStart = false;
			}

			// TODO sync
			this.com.Sync.signalGameIsReady();
		} catch (Throwable e) {
			com.onError(e.getLocalizedMessage(), true);
		}
	}

	@Override
	public void onFrame() {
		try {
			com.onDebugMessage("Frame " + this.frames + " Units " + this.game.getAllUnits().size(), DebugEnum.FRAMES);
			if (shouldExecuteOnFrame()) {
				// Draw info even if paused (at the end)
				if (!endConditionSatisfied && !game.isPaused()) {
					if (this.firstOnFrame) {
						firstExecOnFrame();
						// TODO sync
						com.Sync.signalInitIsDone();
					} else {
						endConditionSatisfied = solveEventsAndCheckEnd();
						com.ComData.setOnFinal(endConditionSatisfied);

						// This signals that the PREVIOUS onFrame was executed
						// com.Sync.signalIsEndCanBeChecked();
					}

					this.frames++;
					showFramesPerSecs();
					updateGameSpeed();

					if (!endConditionSatisfied) {
						ArrayList<GenericAction> actionsToRegister = com.ComData.actionQueue.getQueueAndFlush();

						for (GenericAction action : actionsToRegister) {
							action.registerOnUnitObserver();
							onNewAction(action, (Object[]) null);
						}

						for (Unit rawUnit : self.getUnits()) {
							ArrayList<OnUnitObserver> a = genericObservers.get(rawUnit.getID());

							if (a != null) {
								if (a.size() > 1) {
									com.onSendMessage("NumOfActions: " + a.size());
								}
								int i = 0;
								while (i < a.size()) {
									int lastSize = a.size();
									OnUnitObserver observer = a.get(i);
									observer.onUnit(rawUnit);

									if (lastSize == a.size())
										i++;
									// else en onUnit se llamo a borrar el
									// observador
								}
							}
						}
					}
				}
				printUnitsInfo();
			}
		} catch (Throwable e) {
			com.onError(e.getLocalizedMessage(), true);
		}
	}

	@Override
	public void onUnitDestroy(Unit unit) {
		try {
			com.onDebugMessage("DESTROY " + unit.getType().toString() + " id " + unit.getID() + "at frame " + frames,
					DebugEnum.ON_UNIT_DESTROY);

			for (UnitKilledObserver unitKilledObserver : unitKilledObservers) {
				unitKilledObserver.onUnitKilled(unit);
			}

			super.onUnitDestroy(unit);
		} catch (Throwable e) {
			com.onError(e.getLocalizedMessage(), true);
		}
	}

	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	// Called on the first execution of onFrame
	private void firstExecOnFrame() {
		// TODO OLD
		// for (Unit unit : self.getUnits()) {
		// if (unit.getType().equals(UnitType.Terran_Marine)) {
		// com.ComData.unit = unit;
		// }
		// }
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

	public void registerOnUnitObserver(OnUnitObserver obs) {
		if (genericObservers.containsKey(obs.getUnitObserved().getID())) {
			ArrayList<OnUnitObserver> a = genericObservers.get(obs.getUnitObserved().getID());
			a.add(obs);

			genericObservers.put(obs.getUnitObserved().getID(), a);
		} else {
			ArrayList<OnUnitObserver> a = new ArrayList<>();
			a.add(obs);

			genericObservers.put(obs.getUnitObserved().getID(), a);
		}
	}

	public void unRegisterOnUnitObserver(OnUnitObserver obs) {
		if (genericObservers.containsKey(obs.getUnitObserved().getID())) {
			ArrayList<OnUnitObserver> a = genericObservers.get(obs.getUnitObserved().getID());
			a.remove(obs);

			genericObservers.put(obs.getUnitObserved().getID(), a);
		}
	}

	public void registerUnitKilledObserver(UnitKilledObserver obs) {
		if (!unitKilledObservers.contains(obs))
			unitKilledObservers.add(obs);
	}

	public void unRegisterUnitKilledObserver(UnitKilledObserver obs) {
		if (unitKilledObservers.contains(obs))
			unitKilledObservers.remove(obs);
	}
	/*
	 * public void addEvent(AbstractEvent event) { com.onDebugMessage("EVENT " +
	 * frames, DebugEnum.EVENT_AT_FRAME); this.events.add(event); }
	 */

	public abstract void onEndAction(GenericAction genericAction, Object... args);
	// public abstract double getReward();

	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	@Override
	public void run() {
		mirror.getModule().setEventListener(this);
		mirror.startGame();
	}

	private void printUnitsInfo() {
		// Draw current actions
		for (Unit myUnit : self.getUnits()) {
			if (myUnit.getType().equals(UnitType.Terran_Marine)) {
				game.drawTextMap(myUnit.getPosition().getX(), myUnit.getPosition().getY(),
						"Order: " + myUnit.getOrder() + myUnit.getPosition().toString());
				game.drawLineMap(myUnit.getPosition().getX(), myUnit.getPosition().getY(),
						myUnit.getOrderTargetPosition().getX(), myUnit.getOrderTargetPosition().getY(),
						bwapi.Color.Red);

				// printDanger(myUnit);
			}
		}

		// Draw weapon range
		for (Unit unit : game.getAllUnits()) {
			game.drawCircleMap(unit.getX(), unit.getY(), unit.getType().groundWeapon().maxRange(),
					unit.getPlayer() == self ? Color.Blue : Color.Red);
		}
	}

	private void printDanger(Unit u) {
		// 0 1 2
		// 3 4 5
		// 6 7 8

		// 0
		game.drawBoxMap(u.getX() - 60, u.getY() - 60, u.getX() - 20, u.getY() - 20, Color.Red);
		// 1
		game.drawBoxMap(u.getX() - 20, u.getY() - 60, u.getX() + 20, u.getY() - 20, Color.Red);
		// 2
		game.drawBoxMap(u.getX() + 20, u.getY() - 60, u.getX() + 60, u.getY() - 20, Color.Red);
		// 3
		game.drawBoxMap(u.getX() - 60, u.getY() - 20, u.getX() - 20, u.getY() + 20, Color.Red);
		// 4
		game.drawBoxMap(u.getX() - 20, u.getY() - 20, u.getX() + 20, u.getY() + 20, Color.Red);
		// 5
		game.drawBoxMap(u.getX() + 20, u.getY() - 20, u.getX() + 60, u.getY() + 20, Color.Red);
		// 6
		game.drawBoxMap(u.getX() - 60, u.getY() + 20, u.getX() - 20, u.getY() + 60, Color.Red);
		// 7
		game.drawBoxMap(u.getX() - 20, u.getY() + 20, u.getX() + 20, u.getY() + 60, Color.Red);
		// 8
		game.drawBoxMap(u.getX() - 60, u.getY() + 20, u.getX() + 60, u.getY() + 60, Color.Red);
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
	 * Updates the current game speed so its running at the desired speed (as
	 * set in the gui)
	 */
	private void updateGameSpeed() {
		if (lastFrameSpeed != frameSpeed) {
			lastFrameSpeed = frameSpeed;
			this.game.setLocalSpeed(frameSpeed);
		}
	}
}
