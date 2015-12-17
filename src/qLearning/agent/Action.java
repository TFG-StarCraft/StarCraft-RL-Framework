package qLearning.agent;

import com.Com;

import bot.action.AttackUnitOnSight;
import bot.action.GenericAction;
import bot.action.movement.MoveDown;
import bot.action.movement.MoveLeft;
import bot.action.movement.MoveRight;
import bot.action.movement.MoveUp;

public enum Action {
	UP, DOWN, LEFT, RIGHT, ATTACK;

	public GenericAction toAction(Com com) {
		switch (this) {
		case UP:
			return new MoveUp(com, com.ComData.unit);
		case DOWN:
			return new MoveDown(com, com.ComData.unit);
		case LEFT:
			return new MoveLeft(com, com.ComData.unit);
		case RIGHT:
			return new MoveRight(com, com.ComData.unit);
		case ATTACK:
			return new AttackUnitOnSight(com, com.ComData.unit);
		default:
			throw new IllegalArgumentException("Accion no valida");
		}
	}
}
