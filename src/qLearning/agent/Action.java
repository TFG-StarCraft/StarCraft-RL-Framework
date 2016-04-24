package qLearning.agent;

import java.util.Arrays;

import com.Com;

import bot.action.*;
import bot.action.singleUnit.AttackUnitOnSightLesHP;
import bot.action.singleUnit.movement.*;
import bot.action.singleUnit.movement.relative.*;
import bot.action.singleUnit.movement.relative.relativeToGroup.*;
import utils.Config;

/**
 * Class to manage all the actions.
 * 
 * To use an action, add an enum tag to the ActionEnum enum, and then add the
 * proper case to the toAction method as follows
 * 
 * @author Alberto Casas Ortiz
 * @author Raúl Martín Guadaño
 * @author Miguel Ascanio Gómez
 */
public class Action {

	private static long mask;

	public enum ActionEnum {
		M_UP, M_DOWN, M_LEFT, M_RIGHT,

		M_APPROACH, M_AWAY, M_ARR_COUNTERCLOCKWISE, M_ARR_CLOCKWISE,

		M_ARR_ALLIES_COUNTCLOCKW, M_ARR_ALLIES_CLOCKW, M_ARR_ENE_COUNTCLOCKW, M_ARR_ENE_CLOCKW,

		M_FROM_ALLIES, M_TO_ALLIES, M_FROM_ENE, M_TO_ENE,

		ATTACK_LESS_HP;

		public GenericAction toAction(Com com) {
			switch (this) {
			case M_UP:
				return new MoveUp(com, com.ComData.unit);
			case M_DOWN:
				return new MoveDown(com, com.ComData.unit);
			case M_LEFT:
				return new MoveLeft(com, com.ComData.unit);
			case M_RIGHT:
				return new MoveRight(com, com.ComData.unit);

			case M_APPROACH:
				return new MoveApproach(com, com.ComData.unit);
			case M_AWAY:
				return new MoveAway(com, com.ComData.unit);
			case M_ARR_COUNTERCLOCKWISE:
				return new MoveAroundCounterclockwise(com, com.ComData.unit);
			case M_ARR_CLOCKWISE:
				return new MoveAroundClockwise(com, com.ComData.unit);

			case M_ARR_ENE_COUNTCLOCKW:
				return new MoveAroundEnemiesCounterclockwise(com, com.ComData.unit);
			case M_ARR_ENE_CLOCKW:
				return new MoveAroundEnemiesClockwise(com, com.ComData.unit);
			case M_ARR_ALLIES_COUNTCLOCKW:
				return new MoveAroundAlliesCounterclockwise(com, com.ComData.unit);
			case M_ARR_ALLIES_CLOCKW:
				return new MoveAroundAlliesClockwise(com, com.ComData.unit);

			case M_FROM_ENE:
				return new MoveFromEnemies(com, com.ComData.unit);
			case M_TO_ENE:
				return new MoveToEnemies(com, com.ComData.unit);
			case M_FROM_ALLIES:
				return new MoveFromAllies(com, com.ComData.unit);
			case M_TO_ALLIES:
				return new MoveToAllies(com, com.ComData.unit);

			case ATTACK_LESS_HP:
				return new AttackUnitOnSightLesHP(com, com.ComData.unit);
			default:
				throw new IllegalArgumentException("Accion no valida");
			}
		}

		public long getMask() {
			return 1 << this.ordinal();
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
		Config.set(Config.ACTIONS_DEFAULT, Long.toString(mask));
	}

	public static void removeFromMask(ActionEnum e) {
		mask = mask & ~e.getMask();
		Config.set(Config.ACTIONS_DEFAULT, Long.toString(mask));
	}

	public static void loadMask() {
		try {
			mask = Long.parseLong(Config.get(Config.ACTIONS_DEFAULT));
		} catch (NumberFormatException e) {
			mask = ~0;
		}
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

		Config.set(Config.ACTIONS_DEFAULT, Long.toString(mask));

		// ordinals = Arrays.copyOf(ordinalsAux, numValues);
	}
}
