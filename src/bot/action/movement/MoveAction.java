package bot.action.movement;

import com.Com;

import bot.action.GenericAction;
import bwapi.Position;
import bwapi.Unit;

public abstract class MoveAction implements GenericAction {

	protected final Unit unit;

	protected int iniX;
	protected int iniY;
	protected int endX;
	protected int endY;
	protected int testX;
	protected int testY;

	private long timeEnd;

	private Com com;

	public MoveAction(Com com, Unit unit) {
		this.com = com;
		this.unit = unit;
		iniX = unit.getX();
		iniY = unit.getY();
		this.setUpMove();
		timeEnd = -1;
	}

	@Override
	public void checkAndActuate() {
		//System.out.println(" a " + System.currentTimeMillis());
		//System.out.println(timeEnd);
		if (System.currentTimeMillis() >= timeEnd && timeEnd != -1) {
			onEndAction(false);
		} else {
			if (unit.isMoving()) {
				int a = unit.getOrderTargetPosition().getX();
				int b = unit.getOrderTargetPosition().getY();
				if (a != endX || b != endY) {
					startMove();
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
					startMove();
				}
			}
		}
	}

	private void startMove() {
		if (timeEnd == -1) {
			timeEnd = System.currentTimeMillis() + bot.Const.TIME_MAX;
			this.unit.move(new Position(endX, endY));
		}
	}

	@Override
	public void onEndAction(boolean correct) {
		if (correct) {
			com.ComData.action = null;
			com.ComData.lastActionOk = true;
			com.ComData.unit.removeAction();
			com.Sync.s_postAction.release();
		} else {
			//System.out.println("Miss");
			com.ComData.action = null;
			com.ComData.lastActionOk = false;
			com.ComData.unit.removeAction();
			com.Sync.s_postAction.release();
		}
	}

	protected abstract void setUpMove();

	public boolean isValid() {
		return unit.getPosition().hasPath(new Position(testX, testY));
	}

}
