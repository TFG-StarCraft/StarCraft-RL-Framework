package bot.action.singleUnit.movement;

import com.Com;

import bot.action.singleUnit.GenericUnitAction;
import bwapi.Order;
import bwapi.Position;
import bwapi.Unit;
import newAgent.agent.GenericAgent;
import utils.DebugEnum;

/**
 * MoveAction. Parent of all movement actions. Execute the movement.
 * 
 * @author Alberto Casas Ortiz
 * @author Raúl Martín Guadaño
 * @author Miguel Ascanio Gómez
 */
public abstract class MoveAction extends GenericUnitAction {
	/** Initial pos of the unit. */
	protected int iniX, iniY;
	/** Target pos of the unit. */
	protected int endX, endY;
	/** Movement test position. */
	protected int testX, testY;

	/** True if the order has been given. */
	protected boolean moveOrderHasBeenGiven;

	/***************/
	/* CONSTRUCTOR */
	/***************/

	/**
	 * Constructor of the class MoveAction.
	 * 
	 * @param com
	 *            Comunication.
	 * @param unit
	 *            Unit to move.
	 * @param agentEpoch
	 */
	public MoveAction(GenericAgent agent, Com com, Unit unit) {
		super(agent, com, unit, bot.Const.FRAMES_MOVE, true);
		iniX = unit.getX();
		iniY = unit.getY();
		this.setUpMove();

		moveOrderHasBeenGiven = false;
	}

	/****************/
	/* CLASS METHOD */
	/****************/

	/**
	 * Start an action.
	 */
	protected void startAction() {
		if (!actionStarted) {
			this.frameEnd = com.bot.frames + bot.Const.FRAMES_MOVE;
			this.actionStarted = true;
			this.moveOrderHasBeenGiven = true;
			this.unit.move(new Position(endX, endY));

			super.order = this.unit.getOrder();
		}
	}

	/*******************/
	/* OVERRIDE METHOD */
	/*******************/

	/**
	 * Executes the action.
	 */
	@Override
	public void executeAction() {

		if (moveOrderHasBeenGiven) {
			if (unit.isMoving()) {
				int a = unit.getOrderTargetPosition().getX();
				int b = unit.getOrderTargetPosition().getY();

				if (a != endX || b != endY) {
					startAction();
				} else {
					if (unit.getX() == endX && unit.getY() == endY) {
						com.onDebugMessage("Action OK - In position (1)", DebugEnum.ACTION_OK);
						onEndAction(true);
					}
				}
				// !unitIsMoving
			} else if (unit.getOrder().equals(Order.PlayerGuard) || unit.isAttacking() || unit.isStartingAttack()) {
				if (unit.getX() == endX && unit.getY() == endY) {
					com.onDebugMessage("Action OK - In position (2)", DebugEnum.ACTION_OK);
					onEndAction(true);
				} else {
					com.onDebugMessage("Action Fail - Not in position", DebugEnum.ACTION_FAIL);
					onEndAction(false);
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

	/**
	 * Set if the movement is possible.
	 */
	@Override
	public boolean isPossible() {
		// return unit.getPosition().hasPath(new Position(testX, testY));
		return true;
	}

	/*******************/
	/* ABSTRACT METHOD */
	/*******************/

	/**
	 * This method sets up endX and endY positions
	 */
	protected abstract void setUpMove();

}
