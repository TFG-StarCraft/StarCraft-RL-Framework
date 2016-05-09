package newAgent.event.factories;

import com.Com;

import bot.action.GenericAction;
import newAgent.event.AbstractEvent;

public class AEFDestruirUnidad extends AbstractEventsFactory {

	/**
	 * Expeted args: args[0] : GenericAction action args[1] : Boolean correct
	 */
	public static final int CODE_DEFAULT_ACTION = 1;
	/**
	 * Expeted args: none
	 */
	public static final int CODE_KILL = 2;
	/**
	 * Expeted args: none
	 */
	public static final int CODE_KILL_ALL = 3;
	/**
	 * Expeted args: none
	 */
	public static final int CODE_KILLED = 4;

	private Com com;

	public AEFDestruirUnidad(Com com) {
		this.com = com;
	}

	@Override
	public AbstractEvent newAbstractEvent(int code, Object... args) {
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
			if (args.length != 0) {
				throw new IllegalArgumentException();
			}

			return new AbstractEvent(code) {

				@Override
				public void notifyEvent() {
					com.onSendMessage("Randy ha matado :)");
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
					return true;
				}
			};
		case CODE_KILLED:
			if (args.length != 0) {
				throw new IllegalArgumentException();
			}

			return new AbstractEvent(code) {

				@Override
				public void notifyEvent() {
					com.onSendMessage("Randy ha muerto :(");
					com.onEvent(this);
				}

				@Override
				public boolean isFinalEvent() {
					return true;
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
					return true;
				}
			};
		case CODE_KILL_ALL:
			if (args.length != 0) {
				throw new IllegalArgumentException();
			}

			return new AbstractEvent(code) {

				@Override
				public void notifyEvent() {
					com.onSendMessage("Randy ha matado a todo :)");
					com.onEvent(this);
				}

				@Override
				public boolean isFinalEvent() {
					return true;
				}

				@Override
				public boolean returnsControlToAgent() {
					return true;
				}

				@Override
				public boolean isGoalState() {
					return true;
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
};
