package newAgent.event.factories;

import com.Com;

import bot.action.GenericAction;
import newAgent.event.AbstractEvent;

public class AEFGroup extends AbstractEventsFactory {

	/**
	 * Expeted args: args[0] : GenericAction action args[1] : Boolean correct
	 */
	public static final int CODE_DEFAULT_ACTION = 1;
	/**
	 * Expeted args: args[0] : numOfGroup
	 */
	public static final int CODE_KILL = 2;
	/**
	 * Expeted args: args[0] : numOfGroup
	 */
	public static final int CODE_KILL_ALL = 4;
	/**
	 * Expeted args: args[0] : numOfGroup
	 */
	public static final int CODE_DEAD = 3;
	/**
	 * Expeted args: args[0] : numOfGroup
	 */
	public static final int CODE_DEAD_ALL = 5;

	private Com com;

	public AEFGroup(Com com) {
		this.com = com;
	}

	@Override
	public AbstractEvent newAbstractEvent(int code, Object... args) {
		Integer numGroup;
		
		switch (code) {
		case CODE_DEFAULT_ACTION:
			if (args.length != 2) {
				throw new IllegalArgumentException();
			}
			if (! (args[0] instanceof GenericAction)) {
				throw new IllegalArgumentException();
			}
			if (! (args[1] instanceof Boolean)) {
				throw new IllegalArgumentException();
			}

			return new AbstractEvent(code) {
				//private GenericAction action = (GenericAction) args[0];
				private Boolean correct = (Boolean) args[1];

				@Override
				public void notifyEvent() {
					com.onEvent(this);
				}

				@Override
				public boolean isFinalEvent() {
					return false;
				}

				@Override
				public boolean returnsControlToAgent() {
					return true;
				}

				@Override
				public boolean isGoalState() {
					return false;
				}

				@Override
				public boolean lastActionOk() {
					return correct;
				}
			};
		case CODE_KILL:
			if (args.length != 1) {
				throw new IllegalArgumentException();
			}
			if (! (args[0] instanceof Integer)) {
				throw new IllegalArgumentException();
			}
			numGroup = (Integer) args[0];	
			
			return new AbstractEvent(code) {

				@Override
				public void notifyEvent() {
					com.onSendMessage("Grupo " + numGroup + " ha matado :)");
					com.onEvent(this);
				}

				@Override
				public boolean isFinalEvent() {
					return false;
				}

				@Override
				public boolean returnsControlToAgent() {
					// TODO return control to agent
					return true;
				}

				@Override
				public boolean isGoalState() {
					return false;
				}

				@Override
				public boolean lastActionOk() {
					return true;
				}
			};
		case CODE_DEAD:
			if (args.length != 1) {
				throw new IllegalArgumentException();
			}
			if (! (args[0] instanceof Integer)) {
				throw new IllegalArgumentException();
			}
			numGroup = (Integer) args[0];

			return new AbstractEvent(code) {

				@Override
				public void notifyEvent() {
					com.onSendMessage("Grupo " + numGroup + " ha muerto una unidad :(");
					com.onEvent(this);
				}

				@Override
				public boolean isFinalEvent() {
					return true;
				}

				@Override
				public boolean returnsControlToAgent() {
					// TODO return control to agent
					return true;
				}

				@Override
				public boolean isGoalState() {
					return false;
				}

				@Override
				public boolean lastActionOk() {
					return true;
				}
			};
		case CODE_KILL_ALL:
			if (args.length != 1) {
				throw new IllegalArgumentException();
			}
			if (! (args[0] instanceof Integer)) {
				throw new IllegalArgumentException();
			}
			numGroup = (Integer) args[0];

			return new AbstractEvent(code) {

				@Override
				public void notifyEvent() {
					com.onSendMessage("Grupo " + numGroup + " ha matado a todo :)");
					com.onEvent(this);
				}

				@Override
				public boolean isFinalEvent() {
					return true;
				}

				@Override
				public boolean returnsControlToAgent() {
					// TODO return control to agent
					return true;
				}

				@Override
				public boolean isGoalState() {
					// TODO return isGoal
					return true;
				}

				@Override
				public boolean lastActionOk() {
					return true;
				}
			};
			case CODE_DEAD_ALL:
				if (args.length != 1) {
					throw new IllegalArgumentException();
				}
				if (! (args[0] instanceof Integer)) {
					throw new IllegalArgumentException();
				}
				numGroup = (Integer) args[0];

				return new AbstractEvent(code) {

					@Override
					public void notifyEvent() {
						com.onSendMessage("Grupo " + numGroup + " han muerto a todos :(");
						com.onEvent(this);
					}

					@Override
					public boolean isFinalEvent() {
						return true;
					}

					@Override
					public boolean returnsControlToAgent() {
						// TODO return control to agent
						return true;
					}

					@Override
					public boolean isGoalState() {
						return false;
					}

					@Override
					public boolean lastActionOk() {
						return true;
					}
				};
		default:
			throw new IllegalArgumentException();
		}
	}

}
