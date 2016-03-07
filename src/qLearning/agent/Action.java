package qLearning.agent;

import java.util.Arrays;

import com.Com;

import bot.action.AttackUnitOnSight;
import bot.action.GenericAction;
import bot.action.movement.MoveApproach;
import bot.action.movement.MoveAroundLeft;
import bot.action.movement.MoveAroundRight;
import bot.action.movement.MoveAway;
import bot.action.movement.MoveDown;
import bot.action.movement.MoveLeft;
import bot.action.movement.MoveRight;
import bot.action.movement.MoveUp;

public class Action {

	public enum ActionEnum {
		MOVEAPPROACH, MOVEAWAY, 
		MOVE_ARROUND_LEFT, MOVE_ARROUND_RIGHT,
		MOVE_DOWN, MOVE_LEFT, MOVE_RIGHT, MOVE_UP,
		ATTACK;

		public long getMask() {
			return 1 << this.ordinal();
		}

		public GenericAction toAction(Com com) {
			// TODO
			switch (this) {
			case ATTACK:
				return new AttackUnitOnSight(com, com.ComData.unit);
			case MOVEAPPROACH:
				return new MoveApproach(com, com.ComData.unit);
			case MOVEAWAY:
				return new MoveAway(com, com.ComData.unit);
			case MOVE_ARROUND_LEFT:
				return new MoveAroundLeft(com, com.ComData.unit);
			case MOVE_ARROUND_RIGHT:
				return new MoveAroundRight(com, com.ComData.unit);
			case MOVE_DOWN:
				return new MoveDown(com, com.ComData.unit);
			case MOVE_LEFT:
				return new MoveLeft(com, com.ComData.unit);
			case MOVE_RIGHT:
				return new MoveRight(com, com.ComData.unit);
			case MOVE_UP:
				return new MoveUp(com, com.ComData.unit);
			default:
				throw new IllegalArgumentException("Not valid action");			
			}
		}
	}

	private int ordinal;
	private ActionEnum e;

	public Action(int i) {
		this.e = values[i];
		this.ordinal = ordinals[e.ordinal()];
	}

	public GenericAction toAction(Com com) {
		return this.e.toAction(com);
	}

	public int ordinal() {
		return this.ordinal;
	}

	@Override
	public String toString() {
		return e.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Action && (((Action) obj).e == this.e);
	}

	private static long mask = ~0;
	private static ActionEnum[] values;
	private static int[] ordinals;

	public static ActionEnum[] values() {
		return values;
	}

	public static boolean isSelected(ActionEnum e) {
		return (mask & e.getMask()) != 0;
	}

	public static void addToMask(ActionEnum e) {
		mask = mask | e.getMask();
	}

	public static void removeFromMask(ActionEnum e) {
		mask = mask & ~e.getMask();
	}

	public static void init() {
		ActionEnum[] valuesAux = new ActionEnum[ActionEnum.values().length];
		int ordinalsAux[] = new int[ActionEnum.values().length];
		int numValues = 0;

		for (int i = 0; i < ActionEnum.values().length; i++) {
			if (isSelected(ActionEnum.values()[i])) {
				valuesAux[numValues] = ActionEnum.values()[i];
				ordinalsAux[ActionEnum.values()[i].ordinal()] = numValues;

				numValues++;
			}
		}

		values = Arrays.copyOf(valuesAux, numValues);
		ordinals = ordinalsAux;
		//ordinals = Arrays.copyOf(ordinalsAux, numValues);
	}
}
