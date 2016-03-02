package utils;

public enum DebugEnum {

	ACTION_OK, ACTION_FAIL, FRAME_LIMIT, BAD_ORDER, FRAMES, EVENT_AT_FRAME, ON_UNIT_DESTROY, REWARD;

	public String toString() {
		switch (this) {
		case ACTION_OK:
			return "Action OK";
		case ACTION_FAIL:
			return "Action fail";
		case BAD_ORDER:
			return "Bad order";
		case EVENT_AT_FRAME:
			return "Event at frame";
		case FRAME_LIMIT:
			return "Frame limit reached";
		case FRAMES:
			return "Frames count";
		case ON_UNIT_DESTROY:
			return "On unit destroy";
		case REWARD:
			return "Reward";
		default:
			return null;
		}
	}

	public long getMask() {
		return 1 << this.ordinal();
	}
	
}
