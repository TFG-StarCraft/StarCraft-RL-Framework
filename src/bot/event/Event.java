package bot.event;

public class Event {

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
