package bot.action.movement;

import com.Com;

import bwapi.Unit;

/**
 * Movement. Move up.
 * @author Alberto Casas Ortiz
 * @author Raúl Martín Guadaño
 * @author Miguel Ascanio Gómez
 */
public class MoveUp extends MoveAction {
	
	
	/***************/
	/* CONSTRUCTOR */
	/***************/
	
	/**
	 * Constructor of the class MoveUp.
	 * @param com Comunication.
	 * @param unit Unit to move.
	 * @param agentEpoch 
	 */
	public MoveUp(Com com, Unit unit) {
		super(com, unit);
	}
	
	/*******************/
	/* OVERRIDE METHOD */
	/*******************/
	
	/**
	 * Do the move up movement.
	 */
	@Override
	protected void setUpMove() {
		this.endX = unit.getX();
		this.endY = unit.getY() - bot.Const.STEP;

		this.testX = unit.getX();
		this.testY = unit.getY() - bot.Const.STEP - bot.Const.TEST;
	}

}
