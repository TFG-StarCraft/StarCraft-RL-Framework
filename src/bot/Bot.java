package bot;

import com.Com;

import bot.UnitWrapper.BWAPI_UnitToWrapper;
import bot.UnitWrapper.UnitWrapper;
import bwapi.DefaultBWListener;
import bwapi.Game;
import bwapi.Mirror;
import bwapi.Player;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;

public class Bot extends DefaultBWListener implements Runnable {

	private Com com;

	public Mirror mirror;
	public Game game;
	public Player self;

	private BWAPI_UnitToWrapper unitToWrapper;

	private boolean firstStart;
	private boolean firstExec;
	private boolean restarting;

	public Bot(Com com) {
		this.com = com;
		this.mirror = new Mirror();
		this.firstStart = true;
	}

	@Override
	public void onStart() {
		// onStart is also called after re-start
		this.game = mirror.getGame();
		this.self = game.self();
		// this.game.setGUI(false);
		this.game.setLocalSpeed(0);

		this.firstExec = true;
		this.unitToWrapper = new BWAPI_UnitToWrapper();
		this.restarting = false;

		this.com.ComData.action = null;
		this.com.ComData.enBaliza = false;
		this.com.ComData.restart = false;

		if (firstStart) { // Only enters the very first execution (restarts wont enter here)
			// Use BWTA to analyze map
			// This may take a few minutes if the map is processed first time!
			System.out.println("Analyzing map...");
			BWTA.readMap();
			BWTA.analyze();
			System.out.println("Map data ready");
			this.firstStart = false;
		}

		this.com.Sync.s_restartSync.release();
	}

	@Override
	public void onFrame() {
		if (shouldExecuteOnFrame()) {
			// Draw even if paused (at the end)
			if (!game.isPaused()) {
				if (firstExec) {
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
					com.ComData.enBaliza = false;
					firstExec = false;
					com.Sync.s_initSync.release();
				}
				// Add action
				if (com.ComData.action != null) {
					com.ComData.unit.addAction(com.ComData.action);
				}

				for (Unit rawUnit : self.getUnits()) {
					UnitWrapper unit;
					if (unitToWrapper.contains(rawUnit)) {
						unit = unitToWrapper.get(rawUnit);
					} else {
						unit = new UnitWrapper(rawUnit);
						unitToWrapper.put(unit);
					}

					unit.checkAndDispatchActions();
				}

				// End check
				for (Unit unit : self.getUnits()) {
					if (unit.getType().equals(UnitType.Terran_Command_Center)) {
						com.ComData.enBaliza = unit.distanceTo(com.ComData.unit.getUnit().getPosition()) < 150;
					}
				}
			}
			printUnitsInfo();
		}
	}

	private boolean isRestarting() {
		if (!restarting) {
			if (com.ComData.restart) {
				System.out.println("Restart2...");
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
			} else if (myUnit.getType().equals(UnitType.Terran_Command_Center)) {
				game.drawTextMap(myUnit.getPosition().getX(), myUnit.getPosition().getY(),
						"Dist: " + myUnit.distanceTo(com.ComData.unit.getUnit().getPosition()));
				game.drawLineMap(myUnit.getPosition().getX(), myUnit.getPosition().getY(),
						com.ComData.unit.getUnit().getOrderTargetPosition().getX(),
						com.ComData.unit.getUnit().getOrderTargetPosition().getY(), bwapi.Color.Green);
			}
		}
	}

	@Override
	public void run() {
		mirror.getModule().setEventListener(this);
		mirror.startGame();
	}

}
