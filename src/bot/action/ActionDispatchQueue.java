package bot.action;

import java.util.ArrayList;

import com.Com;

import qLearning.agent.Action;

/**
 * This class is used to send actions from de Agent to the Bot. When the Agent
 * decides that an action should be taken by the Bot, the agent enques taht
 * action onto this queue. The bot, in every onFrame gets this queue and start
 * executing every action in this queue, which is flushed.
 */
public class ActionDispatchQueue {

	private Com com;

	private ArrayList<Action> list;

	public ActionDispatchQueue(Com com) {
		this.com = com;

		this.list = new ArrayList<>();
	}

	public synchronized void enqueueAction(Action action) {
		this.list.add(action);
	}

	public synchronized ArrayList<GenericAction> getQueueAndFlush() {

		ArrayList<GenericAction> r = new ArrayList<GenericAction>();
		for (Action action : list) {
			r.add(action.toAction(com));
		}

		this.list.clear();
		return r;
	}

	public synchronized void clear() {
		this.list.clear();
	}

}
