package qLearning.agent;

import com.Com;

import bot.action.AttackUnitOnSight;
import bot.action.GenericAction;
import bot.action.movement.MoveAroundRight;

public enum Action {
	MOVEAROUNDRIGHT, ATTACK;

	int epoch;
	
	public GenericAction toAction(Com com) {
		switch (this) {

		case MOVEAROUNDRIGHT:
			return new MoveAroundRight(com, com.ComData.unit, epoch);
		case ATTACK:
			return new AttackUnitOnSight(com, com.ComData.unit, epoch);
		default:
			throw new IllegalArgumentException("Accion no valida");
		}
	}
}
