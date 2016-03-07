package bot.action.movement;

import com.Com;

import bwapi.Unit;

/**
 * Movement. Move right.
 * @author Alberto Casas Ortiz
 * @author Raúl Martín Guadaño
 * @author Miguel Ascanio Gómez
 */
public class MoveRight extends MoveAction {	
	
	/***************/
	/* CONSTRUCTOR */
	/***************/
	
	/**
	 * Constructor of the class MoveRight.
	 * @param com Comunication.
	 * @param unit Unit to move.
	 * @param agentEpoch 
	 */
	public MoveRight(Com com, Unit unit) {
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
		this.endX = unit.getX() + bot.Const.STEP;
		this.endY = unit.getY();

		this.testX = unit.getX() + bot.Const.STEP + bot.Const.TEST;
		this.testY = unit.getY();
	}

}
