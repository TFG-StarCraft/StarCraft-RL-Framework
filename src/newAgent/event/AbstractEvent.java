package newAgent.event;

import java.util.Comparator;

/**
 * Objects of this class will be created after some events happen in the game.
 * For now, events are created when a unit ends moving, and when an unit is
 * destroyed.
 */
public abstract class AbstractEvent {

	private final int code;

	public AbstractEvent(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

	public abstract void solveEvent();

	public abstract boolean isFinalEvent();
	
	public abstract boolean isGoalState();
	
	public abstract boolean returnsControlToAgent();
	
	public abstract boolean lastActionOk();

	public static Comparator<AbstractEvent> getPrioCompAscend() {
		return new Comparator<AbstractEvent>() {
			
			@Override
			public int compare(AbstractEvent o1, AbstractEvent o2) {
				return o1.code - o2.code;
			}
		};
	}
	
	public static Comparator<AbstractEvent> getPrioCompDescend() {
		return new Comparator<AbstractEvent>() {
			
			@Override
			public int compare(AbstractEvent o1, AbstractEvent o2) {
				return o2.code - o1.code;
			}
		};
	}
}
