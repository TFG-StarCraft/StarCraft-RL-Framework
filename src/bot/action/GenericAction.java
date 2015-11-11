package bot.action;

public interface GenericAction {

	public void checkAndActuate();
	public void onEndAction(boolean correct);
	public boolean isPossible();
	
}
