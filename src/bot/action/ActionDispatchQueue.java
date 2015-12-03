package bot.action;

import java.util.ArrayList;

import com.Com;

import qLearning.agent.Action;

public class ActionDispatchQueue {

	private Com com;

	private ArrayList<Action> list;

	public ActionDispatchQueue(Com com) {
		this.com = com;

		this.list = new ArrayList<>();
	}

	public synchronized void put(Action action) {
		this.list.add(action);
	}

	public synchronized ArrayList<GenericAction> getQueue() {

		ArrayList<GenericAction> r = new ArrayList<GenericAction>();
		for (Action action : list) {
			r.add(action.toAction(com));
		}

		this.list.clear();
		return r;
	}

}
