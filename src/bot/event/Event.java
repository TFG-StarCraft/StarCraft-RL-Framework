package bot.event;

/**
 * Objects of this class will be created after some events happen in the game.
 * For now, events are created when a unit ends moving, and when an unit is
 * destroyed.
 */
public class Event {

	// TODO hacer que en esta clase se controle todo lo que produce el evento,
	// que no sea un simpe numero

	public static final int CODE_MOVE = 1;
	public static final int CODE_KILL = 2;
	public static final int CODE_KILLED = 3;

	private final int code;

	public Event(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

}
