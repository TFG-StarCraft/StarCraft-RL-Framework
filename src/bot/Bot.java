package bot;

import java.util.ArrayList;
import java.util.HashMap;

import com.Com;

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
import newAgent.Master;
import utils.DebugEnum;

public class Bot extends DefaultBWListener implements Runnable {

	private Com com;

	private Mirror mirror;
	public Game game;
	public Player self;

	private boolean firstOnStart;
	private boolean firstOnFrame;
	private boolean restarting;
	private boolean endFlag;

	public boolean guiEnabled;
	public long frames;
	public int frameSpeed;
	private int lastFrameSpeed;

	// Maps units (with its unique id) to an arrayList of observers
	private HashMap<Integer, ArrayList<OnUnitObserver>> onUnitObservers;
	private ArrayList<UnitKilledObserver> onUnitKilledObservers;
	
	private Master master;

	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	public Bot(Com com, Master master) {
		this.com = com;
		this.master = master;
		this.mirror = new Mirror();
		this.firstOnStart = true;
		this.guiEnabled = true;

		this.frameSpeed = 0;
		this.lastFrameSpeed = frameSpeed;

		this.onUnitObservers = new HashMap<>();
		this.onUnitKilledObservers = new ArrayList<>();
	}

	///////////////////////////////////////////////////////////////////////////
	// BWAPI CALLS ////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	@Override
	public void onStart() {
		try {
			this.initFrame = 0;
			// onStart is also called after re-start
			this.game = mirror.getGame();
			this.self = game.self();
			this.game.setGUI(guiEnabled);
			this.game.setLocalSpeed(frameSpeed);

			this.frames = 0;
			this.firstOnFrame = true;
			this.restarting = false;
			this.restartFlag = false;
			this.endFlag = false;

			//com.ComData.actionQueue.clear();
			this.master.onStart();			

			this.onUnitObservers.clear();
			this.onUnitKilledObservers.clear();

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

			//this.master.signalGameIsReady();
		} catch (Throwable e) {
			e.printStackTrace();
			com.onError(e.getLocalizedMessage(), true);
		}
	}

	private int initFrame = 0;
	
	@Override
	public void onFrame() {
		if (initFrame == 0)
			game.pauseGame();
		if (initFrame < 200) {
			initFrame++;
			return;
		}
		// Pause 200 frames
		if (initFrame == 200)
			game.resumeGame();

		try {
			com.onDebugMessage("Frame " + this.frames + " Units " + this.game.getAllUnits().size(), DebugEnum.FRAMES);
			if (shouldExecuteOnFrame()) {
				// Draw info even if paused (at the end)
				if (!endFlag && !game.isPaused()) {
					if (this.firstOnFrame) {
						firstExecOnFrame();
						master.signalInitIsDone();
					} else {
						// Check end, all agents will check end condition, and
						// if all agents end, master will cause bot to restart
						endFlag = master.solveEventsAndCheckEnd();
					}

					this.frames++;
					showFramesPerSecs();
					updateGameSpeed();

					synchronized (this) {
						if (!endFlag) {
							// Register pending actions for every agent
							master.onFrame();
	
							// Call all unitsObserver for each unit
							for (Unit rawUnit : self.getUnits()) {
								ArrayList<OnUnitObserver> a = onUnitObservers.get(rawUnit.getID());
	
								if (a != null) {
									if (a.size() > 1) {
										com.onSendMessage("NumOfObservers: " + a.size());
										com.onError("NumOfObservers: " + a.size(), false);
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
				}
				printUnitsInfo();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			com.onError(e.getLocalizedMessage(), true);
		}
	}

	@Override
	/**
	 * Calls every registered unitKilledObserver
	 */
	public void onUnitDestroy(Unit unit) {
		try {
			com.onDebugMessage("DESTROY " + unit.getType().toString() + " id " + unit.getID() + "at frame " + frames,
					DebugEnum.ON_UNIT_DESTROY);

			for (UnitKilledObserver unitKilledObserver : onUnitKilledObservers) {
				unitKilledObserver.onUnitKilled(unit);
			}

			super.onUnitDestroy(unit);
		} catch (Throwable e) {
			e.printStackTrace();
			com.onError(e.getLocalizedMessage(), true);
		}
	}

	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	// Called on the first execution of onFrame
	private void firstExecOnFrame() {
		this.master.onFirstFrame();
		this.firstOnFrame = false;
	}

	private boolean restartFlag;
	
	public synchronized void requestRestart() {
		this.restartFlag = true;
	}
	
	/**
	 * Checks if the com.ComData.restar flag is true; and if so restarts the
	 * game
	 * 
	 * @return true if the game is restarting or intended to be
	 */
	private boolean isRestarting() {
		if (!this.restarting && this.restartFlag) {
			com.onSendMessage("Restart bot call...");
			this.restarting = true;
			game.restartGame();
		}

		return restarting;
	}

	private boolean shouldExecuteOnFrame() {
		return !game.isReplay() && !isRestarting();
	}

	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	public synchronized void registerOnUnitObserver(OnUnitObserver obs) {
		if (onUnitObservers.containsKey(obs.getUnitObserved().getID())) {
			ArrayList<OnUnitObserver> a = onUnitObservers.get(obs.getUnitObserved().getID());
			a.add(obs);

			onUnitObservers.put(obs.getUnitObserved().getID(), a);
		} else {
			ArrayList<OnUnitObserver> a = new ArrayList<>();
			a.add(obs);

			onUnitObservers.put(obs.getUnitObserved().getID(), a);
		}
	}

	public synchronized void unRegisterOnUnitObserver(OnUnitObserver obs) {
		if (onUnitObservers.containsKey(obs.getUnitObserved().getID())) {
			ArrayList<OnUnitObserver> a = onUnitObservers.get(obs.getUnitObserved().getID());
			//a.remove(obs);
			// TODO
			a.clear();
			onUnitObservers.put(obs.getUnitObserved().getID(), a);
		}
	}

	public synchronized void registerUnitKilledObserver(UnitKilledObserver obs) {
		if (!onUnitKilledObservers.contains(obs))
			onUnitKilledObservers.add(obs);
	}

	public synchronized void unRegisterUnitKilledObserver(UnitKilledObserver obs) {
		if (onUnitKilledObservers.contains(obs))
			onUnitKilledObservers.remove(obs);
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

//	private void printDanger(Unit u) {
//		// 0 1 2
//		// 3 4 5
//		// 6 7 8
//
//		// 0
//		game.drawBoxMap(u.getX() - 60, u.getY() - 60, u.getX() - 20, u.getY() - 20, Color.Red);
//		// 1
//		game.drawBoxMap(u.getX() - 20, u.getY() - 60, u.getX() + 20, u.getY() - 20, Color.Red);
//		// 2
//		game.drawBoxMap(u.getX() + 20, u.getY() - 60, u.getX() + 60, u.getY() - 20, Color.Red);
//		// 3
//		game.drawBoxMap(u.getX() - 60, u.getY() - 20, u.getX() - 20, u.getY() + 20, Color.Red);
//		// 4
//		game.drawBoxMap(u.getX() - 20, u.getY() - 20, u.getX() + 20, u.getY() + 20, Color.Red);
//		// 5
//		game.drawBoxMap(u.getX() + 20, u.getY() - 20, u.getX() + 60, u.getY() + 20, Color.Red);
//		// 6
//		game.drawBoxMap(u.getX() - 60, u.getY() + 20, u.getX() - 20, u.getY() + 60, Color.Red);
//		// 7
//		game.drawBoxMap(u.getX() - 20, u.getY() + 20, u.getX() + 20, u.getY() + 60, Color.Red);
//		// 8
//		game.drawBoxMap(u.getX() - 60, u.getY() + 20, u.getX() + 60, u.getY() + 60, Color.Red);
//	}

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
