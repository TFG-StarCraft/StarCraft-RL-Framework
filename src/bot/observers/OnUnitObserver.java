package bot.observers;

import bwapi.Unit;

public interface OnUnitObserver {
	/**
	 * This method is meant to be called on each frame of the game (via the
	 * onFrame method in BWAPI.Bot) for each unit of the player.
	 * 
	 * @param unit
	 */
	public void onUnit(Unit unit);

	/**
	 * @return the unit that is being observed by this observer
	 */
	public Unit getUnitObserved();

	/**
	 * Registers this observer into the bot.Bot units listener
	 */
	public void registerOnUnitObserver();

	/**
	 * Unregisters this observer from the bot.Bot units listener
	 */
	public void unRegisterOnUnitObserver();

}
