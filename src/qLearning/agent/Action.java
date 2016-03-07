package qLearning.agent;

import com.Com;

import bot.action.AttackUnitOnSight;
import bot.action.GenericAction;
import bot.action.relativeMovement.MoveApproach;
import bot.action.relativeMovement.MoveAway;
import bot.action.relativeMovement.RelativeToGroup.MoveAroundEnemiesLeft;
import bot.action.relativeMovement.RelativeToGroup.MoveToAllies;

public enum Action {
	MOVEAPPROACHALLIES, ATTACK;
	
	public GenericAction toAction(Com com) {
		switch (this) {

		
		case MOVEAPPROACHALLIES:
			return new MoveAroundEnemiesLeft(com, com.ComData.unit);
		case ATTACK:
			return new AttackUnitOnSight(com, com.ComData.unit);
		default:
			throw new IllegalArgumentException("Accion no valida");
		}
	}
}
