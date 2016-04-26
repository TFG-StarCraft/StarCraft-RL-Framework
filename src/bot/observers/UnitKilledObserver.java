package bot.observers;

import bwapi.Unit;

public interface UnitKilledObserver {
	/**
	 * This method is meant to be called for each unit that is killed in a
	 * frame
	 * 
	 * @param unit killed
	 */
	public void onUnitKilled(Unit unit);

	/**
	 * Registers this observer into the bot.Bot units listener
	 */
	public void registerUnitKilledObserver();

	/**
	 * Unregisters this observer from the bot.Bot units listener
	 */
	public void unRegisterUnitKilledObserver();

}
