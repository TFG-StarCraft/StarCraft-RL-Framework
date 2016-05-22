package bot.action.singleUnit.movement;

import com.Com;

import bwapi.Unit;
import newAgent.agent.OnEndActionObserver;

/**
 * Movement. Move down.
 * @author Alberto Casas Ortiz
 * @author Raúl Martín Guadaño
 * @author Miguel Ascanio Gómez
 */
public class MoveDown extends MoveAction {
		
	/***************/
	/* CONSTRUCTOR */
	/***************/
	
	/**
	 * Constructor of the class MoveDown.
	 * @param com Comunication.
	 * @param unit Unit to move.
	 * @param agentEpoch 
	 */
	public MoveDown(OnEndActionObserver agent, Com com, Unit unit) {
		super(agent, com, unit);
	}	
	
	/*******************/
	/* OVERRIDE METHOD */
	/*******************/
	
	/**
	 * Do the move down movement.
	 */
	@Override
	protected void setUpMove() {
		this.endX = unit.getX();
		this.endY = unit.getY() + bot.Const.STEP;
		this.testX = unit.getX();
		this.testY = unit.getY() + bot.Const.STEP + bot.Const.TEST;
	}

}
