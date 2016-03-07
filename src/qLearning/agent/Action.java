package qLearning.agent;

import com.Com;

import bot.action.AttackUnitOnSight;
import bot.action.GenericAction;
import bot.action.movement.MoveApproach;
import bot.action.movement.MoveAway;

public enum Action {
	MOVEAWAY, MOVEAPPROACH, ATTACK;
	
	public GenericAction toAction(Com com) {
		switch (this) {

		case MOVEAWAY:
			return new MoveAway(com, com.ComData.unit);
		case MOVEAPPROACH:
			return new MoveApproach(com, com.ComData.unit);
		case ATTACK:
			return new AttackUnitOnSight(com, com.ComData.unit);
		default:
			throw new IllegalArgumentException("Accion no valida");
		}
	}
}
