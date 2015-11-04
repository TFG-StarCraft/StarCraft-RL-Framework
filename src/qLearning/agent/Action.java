package qLearning.agent;

import com.Com;

import bot.action.movement.MoveAction;
import bot.action.movement.MoveDown;
import bot.action.movement.MoveLeft;
import bot.action.movement.MoveRight;
import bot.action.movement.MoveUp;

public enum Action {
	UP, DOWN, LEFT, RIGHT;

	public MoveAction toMoveAction(Com com) {
		switch (this) {
		case UP:
			return new MoveUp(com, com.ComData.unit.getUnit());
		case DOWN:
			return new MoveDown(com, com.ComData.unit.getUnit());
		case LEFT:
			return new MoveLeft(com, com.ComData.unit.getUnit());
		case RIGHT:
			return new MoveRight(com, com.ComData.unit.getUnit());
		default:
			throw new IllegalArgumentException("Accion no valida");
		}
	}
}
