package bot.action.movement;

import com.Com;

import bwapi.Unit;

public class MoveUp extends MoveAction {

	public MoveUp(Com com, Unit unit) {
		super(com, unit);
	}

	@Override
	protected void setUpMove() {
		this.endX = unit.getX();
		this.endY = unit.getY() - bot.Const.STEP;

		this.testX = unit.getX();
		this.testY = unit.getY() - bot.Const.STEP - bot.Const.TEST;
	}

}
