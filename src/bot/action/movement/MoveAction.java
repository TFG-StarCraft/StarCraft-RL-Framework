package bot.action.movement;

import com.Com;

import bot.action.GenericAction;
import bot.event.Event;
import bot.observers.unit.GenericUnitObserver;
import bwapi.Position;
import bwapi.Unit;

public abstract class MoveAction implements GenericAction, GenericUnitObserver {

	protected final Unit unit;

	protected int iniX;
	protected int iniY;
	protected int endX;
	protected int endY;
	protected int testX;
	protected int testY;

	private long frameEnd;
	private boolean movStarted;

	private Com com;

	@Override
	public void onUnit(Unit unit) {
		checkAndActuate();
	}
	
	@Override
	public Unit getUnit() {
		return this.unit;
	}
	
	public MoveAction(Com com, Unit unit) {
		this.com = com;
		this.unit = unit;
		iniX = unit.getX();
		iniY = unit.getY();
		this.setUpMove();
		
		this.movStarted = false;
	}

	@Override
	public void checkAndActuate() {
		//System.out.println(" a " + System.currentTimeMillis());
		System.out.println(com.bot.frames);

		if (com.bot.frames >= frameEnd && movStarted) {
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
		if (!movStarted) {
			this.frameEnd = com.bot.frames + 50;
			this.movStarted = true;
			this.unit.move(new Position(endX, endY));
		}
	}

	@Override
	public void onEndAction(boolean correct) {
		com.bot.addEvent(new Event(Event.CODE_MOVE));
		
		if (correct) {
			com.ComData.action = null;
			com.ComData.lastActionOk = true;
			com.ComData.unit.removeAction();
			com.Sync.s_postAction.release();
		} else {
			com.ComData.action = null;
			com.ComData.lastActionOk = false;
			com.ComData.unit.removeAction();
			com.Sync.s_postAction.release();
		}
	}

	protected abstract void setUpMove();

	@Override
	public boolean isPossible() {
		return unit.getPosition().hasPath(new Position(testX, testY));
	}
	
	@Override
	public void register() {
		this.com.bot.registerOnUnitObserver(this);	
	}

}
