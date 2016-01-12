package bot.action.movement;

import com.Com;

import bwapi.Unit;

public class MoveDown extends MoveAction {

	public MoveDown(Com com, Unit unit, int e) {
		super(com, unit, e);
	}

	@Override
	protected void setUpMove() {
		this.endX = unit.getX();
		this.endY = unit.getY() + bot.Const.STEP;
		this.testX = unit.getX();
		this.testY = unit.getY() + bot.Const.STEP + bot.Const.TEST;
	}

}
