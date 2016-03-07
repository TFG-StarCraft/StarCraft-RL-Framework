package qLearning.agent;

import com.Com;

import bot.action.AttackUnitOnSight;
import bot.action.GenericAction;
import bot.action.movement.MoveDown;
import bot.action.movement.MoveLeft;
import bot.action.movement.MoveRight;
import bot.action.movement.MoveUp;
import bot.action.relativeMovement.MoveApproach;
import bot.action.relativeMovement.MoveAroundClockwise;
import bot.action.relativeMovement.MoveAroundLCounterclockwise;
import bot.action.relativeMovement.MoveAway;
import bot.action.relativeMovement.RelativeToGroup.MoveAroundAlliesClockwise;
import bot.action.relativeMovement.RelativeToGroup.MoveAroundAlliesCounterclockwise;
import bot.action.relativeMovement.RelativeToGroup.MoveAroundEnemiesClockwise;
import bot.action.relativeMovement.RelativeToGroup.MoveAroundEnemiesCounterclockwise;
import bot.action.relativeMovement.RelativeToGroup.MoveFromAllies;
import bot.action.relativeMovement.RelativeToGroup.MoveFromEnemies;
import bot.action.relativeMovement.RelativeToGroup.MoveToAllies;
import bot.action.relativeMovement.RelativeToGroup.MoveToEnemies;

public enum Action {
	/*MOVEUP, MOVEDOWN, MOVELEFT, MOVERIGHT,
	MOVEAPPROACH, MOVEAWAY, COUNTERCLOCKWISE, MOVEAROUNDCLOCKWISE,
	MOVEAROUNDALLIESCOUNTERCLOCKWISE, MOVEAROUNDALLIESCLOCKWISE, MOVEAROUNDENEMIESCOUNTERCLOCKWISE, MOVEAROUNDENEMIESCLOCKWISE, 
	*/
	MOVEFROMALLIES, MOVETOALLIES, MOVEFROMENEMIES, MOVETOENEMIES,
	ATTACK;
	
	public GenericAction toAction(Com com) {
		switch (this) {
		/*case MOVEUP: return new MoveUp(com, com.ComData.unit);
		case MOVEDOWN: return new MoveDown(com, com.ComData.unit);
		case MOVELEFT: return new MoveLeft(com, com.ComData.unit);
		case MOVERIGHT: return new MoveRight(com, com.ComData.unit);
		
		case MOVEAPPROACH: return new MoveApproach(com, com.ComData.unit);
		case MOVEAWAY: return new MoveAway(com, com.ComData.unit);
		case COUNTERCLOCKWISE: return new MoveAroundLCounterclockwise(com, com.ComData.unit);
		case MOVEAROUNDCLOCKWISE: return new MoveAroundClockwise(com, com.ComData.unit);
		
		case MOVEAROUNDENEMIESCOUNTERCLOCKWISE: return new MoveAroundEnemiesCounterclockwise(com, com.ComData.unit);
		case MOVEAROUNDENEMIESCLOCKWISE: return new MoveAroundEnemiesClockwise(com, com.ComData.unit);
		case MOVEAROUNDALLIESCOUNTERCLOCKWISE: return new MoveAroundAlliesCounterclockwise(com, com.ComData.unit);
		case MOVEAROUNDALLIESCLOCKWISE: return new MoveAroundAlliesClockwise(com, com.ComData.unit);
		*/
		case MOVEFROMENEMIES: return new MoveFromEnemies(com, com.ComData.unit);
		case MOVETOENEMIES: return new MoveToEnemies(com, com.ComData.unit);
		case MOVEFROMALLIES: return new MoveFromAllies(com, com.ComData.unit);
		case MOVETOALLIES: return new MoveToAllies(com, com.ComData.unit);
		
		
		case ATTACK: return new AttackUnitOnSight(com, com.ComData.unit);
		default:
			throw new IllegalArgumentException("Accion no valida");
		}
	}
}
