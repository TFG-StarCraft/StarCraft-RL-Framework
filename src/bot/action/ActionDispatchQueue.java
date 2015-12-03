package bot.action;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import com.Com;

import qLearning.agent.Action;

public class ActionDispatchQueue {

	private Com com;
	
	private ArrayList<Action> list;
	private Semaphore sem;
	
	public ActionDispatchQueue(Com com) {
		this.com = com;
		
		this.list = new ArrayList<>();
		
		this.sem = new Semaphore(1);
	}

	public void put(Action action) {
		try {
			this.sem.acquire();
		} catch (InterruptedException e) {
			com.onError(e.getLocalizedMessage(), true);
		}
		
		this.list.add(action);
		
		this.sem.release();
	}
	
	public ArrayList<GenericAction> getQueue() {
		try {
			this.sem.acquire();
		} catch (InterruptedException e) {
			com.onError(e.getLocalizedMessage(), true);
		}
		
		ArrayList<GenericAction> r = new ArrayList<GenericAction>();
		for (Action action : list) {
			r.add(action.toAction(com));
		}
		
		this.list.clear();		
		this.sem.release();
		
		return r;
	}
	
}
