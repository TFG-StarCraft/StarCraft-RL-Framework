package newAgent;

import java.util.Arrays;

import com.Com;

import bot.action.*;
import bot.action.singleUnit.AttackUnitOnSightLesHP;
import bot.action.singleUnit.movement.*;
import bot.action.singleUnit.movement.relative.*;
import bot.action.singleUnit.movement.relative.relativeToGroup.*;
import bwapi.Unit;
import newAgent.agent.GenericAgent;
import newAgent.agent.group.MarineGroupAgent;
import newAgent.agent.unit.UnitAgent;
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

		ATTACK_LESS_HP,
		
		///////////////////////////////////////// 
		// Group
		///////////////////////////////////////// 

		G_UP, G_DOWN, G_LEFT, G_RIGHT
		
		;

		public GenericAction toAction(Com com, GenericAgent agent) {
			if (agent instanceof UnitAgent) {
				Unit unit = ((UnitAgent) agent).getUnitObserved();
				switch (this) {
				case M_UP:
					return new MoveUp(agent, com, unit);
				case M_DOWN:
					return new MoveDown(agent, com, unit);
				case M_LEFT:
					return new MoveLeft(agent, com, unit);
				case M_RIGHT:
					return new MoveRight(agent, com, unit);
	
				case M_APPROACH:
					return new MoveApproach(agent, com, unit);
				case M_AWAY:
					return new MoveAway(agent, com, unit);
				case M_ARR_COUNTERCLOCKWISE:
					return new MoveAroundCounterclockwise(agent, com, unit);
				case M_ARR_CLOCKWISE:
					return new MoveAroundClockwise(agent, com, unit);
	
				case M_ARR_ENE_COUNTCLOCKW:
					return new MoveAroundEnemiesCounterclockwise(agent, com, unit);
				case M_ARR_ENE_CLOCKW:
					return new MoveAroundEnemiesClockwise(agent, com, unit);
				case M_ARR_ALLIES_COUNTCLOCKW:
					return new MoveAroundAlliesCounterclockwise(agent, com, unit);
				case M_ARR_ALLIES_CLOCKW:
					return new MoveAroundAlliesClockwise(agent, com, unit);
	
				case M_FROM_ENE:
					return new MoveFromEnemies(agent, com, unit);
				case M_TO_ENE:
					return new MoveToEnemies(agent, com, unit);
				case M_FROM_ALLIES:
					return new MoveFromAllies(agent, com, unit);
				case M_TO_ALLIES:
					return new MoveToAllies(agent, com, unit);
	
				case ATTACK_LESS_HP:
					return new AttackUnitOnSightLesHP(agent, com, unit);
				default:
					throw new IllegalArgumentException("Accion no valida");
				}
			} else if (agent instanceof MarineGroupAgent) {
				switch (this) {
				case G_UP:
					
					break;

				default:
					break;
				}
			} else {
				throw new IllegalArgumentException("Agente desconocido");
			}
		}

		public long getMask() {
			return 1 << this.ordinal();
		}
	}

	private int ordinal;
	private ActionEnum e;
	private GenericAgent agent;

	public Action(int i, GenericAgent agent) {
		this.e = values[i];
		this.agent = agent;
		this.ordinal = ordinals[e.ordinal()];
	}

	public GenericAction toAction(Com com) {
		return this.e.toAction(com, agent);
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
