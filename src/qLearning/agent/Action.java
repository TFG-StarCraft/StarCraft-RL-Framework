package qLearning.agent;

import com.Com;

import bot.action.AttackUnitOnSight;
import bot.action.GenericAction;
import bot.action.movement.MoveApproach;
import bot.action.movement.MoveAway;

public enum Action {
	ATTACK, APPROACH, AWAY;

	int epoch;
	
	public GenericAction toAction(Com com) {
		switch (this) {

		case ATTACK:
			return new AttackUnitOnSight(com, com.ComData.unit, epoch);
		case APPROACH:
			return new MoveApproach(com, com.ComData.unit, epoch);
		case AWAY:
			return new MoveAway(com, com.ComData.unit, epoch);
		default:
			throw new IllegalArgumentException("Accion no valida");
		}
	}
}
