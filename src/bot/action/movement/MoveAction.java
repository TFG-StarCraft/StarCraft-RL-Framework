package bot.action.movement;

import com.Com;

import bot.action.GenericAction;
import bot.event.Event;
import bwapi.Order;
import bwapi.Position;
import bwapi.Unit;

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

	public MoveAction(Com com, Unit unit) {
		super(com, unit, bot.Const.FRAMES_MOVE, true);
		iniX = unit.getX();
		iniY = unit.getY();
		this.setUpMove();

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
					System.out.println("true");
					onEndAction(true);
				} else {
					System.out.println("false");
					onEndAction(false);
				}
			} else if (unit.isMoving()) {
				int a = unit.getOrderTargetPosition().getX();
				int b = unit.getOrderTargetPosition().getY();

				if (a != endX || b != endY) {
					startAction();
				} else {
					if (unit.getX() == endX && unit.getY() == endY) {
						onEndAction(true);
					}
				}
			} else {
				if (unit.getX() == endX && unit.getY() == endY) {
					onEndAction(true);
				} else {
					// No se está ejecutando esta acción
					startAction();
				}
			}
		} else {
			if (unit.getX() == endX && unit.getY() == endY) {
				onEndAction(true);
			} else {
				// No se está ejecutando esta acción
				startAction();
			}
		}
	}

	@Override
	public void onEndAction(boolean correct) {
		com.bot.addEvent(new Event(Event.CODE_MOVE));
		unRegisterUnitObserver();

		if (correct) {
			com.ComData.lastActionOk = true;
			com.Sync.signalActionEnded();
		} else {
			com.ComData.lastActionOk = false;
			com.Sync.signalActionEnded();
		}
	}

	@Override
	public boolean isPossible() {
		//return unit.getPosition().hasPath(new Position(testX, testY));
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
