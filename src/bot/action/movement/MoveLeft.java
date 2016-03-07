package bot.action.movement;

import com.Com;

import bwapi.Unit;

/**
 * Movement. Move left.
 * @author Alberto Casas Ortiz.
 * @author Raúl Martín Guadaño
 * @author Miguel Ascanio Gómez.
 */
public class MoveLeft extends MoveAction {
	
	
	/***************/
	/* CONSTRUCTOR */
	/***************/
	
	/**
	 * Constructor of the class MoveLeft.
	 * @param com Comunication.
	 * @param unit Unit to move.
	 * @param agentEpoch 
	 */
	public MoveLeft(Com com, Unit unit) {
		super(com, unit);
	}
	
	/*******************/
	/* OVERRIDE METHOD */
	/*******************/
	
	/**
	 * Do the move down movement.
	 */
	@Override
	protected void setUpMove() {
		this.endX = unit.getX() - bot.Const.STEP;
		this.endY = unit.getY();
		this.testX = unit.getX() - bot.Const.STEP - bot.Const.TEST;
		this.testY = unit.getY();
	}

}
