package bot.action.movement;

import com.Com;

import bot.action.GenericAction;
import bwapi.Order;
import bwapi.Position;
import bwapi.Unit;
import utils.DebugEnum;

public abstract class MoveAction extends GenericAction {

	// Initial pos of the unit
	protected int iniX;
	protected int iniY;
	// Target pos of the unit
	protected int endX;
	protected int endY;
	// Movement test position
	protected int testX;
	protected int testY;

	protected boolean moveOrderHasBeenGiven;

	public MoveAction(Com com, Unit unit, int agentEpoch) {
		super(com, unit, bot.Const.FRAMES_MOVE, true);
		iniX = unit.getX();
		iniY = unit.getY();
		this.setUpMove();
		
		super.agentEpochCreate = agentEpoch;

		moveOrderHasBeenGiven = false;
	}

	/**
	 * This method sets up endX and endY positions
	 */
	protected abstract void setUpMove();

	@Override
	public void executeAction() {

		if (moveOrderHasBeenGiven) {
			if (unit.getOrder().equals(Order.PlayerGuard) || unit.isAttacking() || unit.isStartingAttack()) {
				if (unit.getX() == endX && unit.getY() == endY) {
					com.onDebugMessage("Action OK - In position (1)", DebugEnum.ACTION_OK);
					onEndAction(true);
				} else {
					com.onDebugMessage("Action Fail - Not in position", DebugEnum.ACTION_FAIL);
					onEndAction(false);
				}
			} else if (unit.isMoving()) {
				int a = unit.getOrderTargetPosition().getX();
				int b = unit.getOrderTargetPosition().getY();

				if (a != endX || b != endY) {
					startAction();
				} else {
					if (unit.getX() == endX && unit.getY() == endY) {
						com.onDebugMessage("Action OK - In position (2)", DebugEnum.ACTION_OK);
						onEndAction(true);
					}
				}
			} else {
				if (unit.getX() == endX && unit.getY() == endY) {
					com.onDebugMessage("Action OK - In position (3)", DebugEnum.ACTION_OK);
					onEndAction(true);
				} else {
					// No se está ejecutando esta acción
					startAction();
				}
			}
		} else {
			if (unit.getX() == endX && unit.getY() == endY) {
				com.onDebugMessage("Action OK - In position (4)", DebugEnum.ACTION_OK);
				onEndAction(true);
			} else {
				// No se está ejecutando esta acción
				startAction();
			}
		}
	}

	@Override
	public boolean isPossible() {
		// return unit.getPosition().hasPath(new Position(testX, testY));
		return true;
	}

	protected void startAction() {
		if (!actionStarted) {
			this.frameEnd = com.bot.frames + bot.Const.FRAMES_MOVE;
			this.actionStarted = true;
			this.moveOrderHasBeenGiven = true;
			this.unit.move(new Position(endX, endY));

			super.order = this.unit.getOrder();
		}
	}

}
