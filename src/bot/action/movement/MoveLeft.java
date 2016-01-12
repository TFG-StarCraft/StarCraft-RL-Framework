package bot.action.movement;

import com.Com;

import bwapi.Unit;

public class MoveLeft extends MoveAction {

	public MoveLeft(Com com, Unit unit, int e) {
		super(com, unit, e);
	}

	@Override
	protected void setUpMove() {
		this.endX = unit.getX() - bot.Const.STEP;
		this.endY = unit.getY();
		this.testX = unit.getX() - bot.Const.STEP - bot.Const.TEST;
		this.testY = unit.getY();
	}

}
