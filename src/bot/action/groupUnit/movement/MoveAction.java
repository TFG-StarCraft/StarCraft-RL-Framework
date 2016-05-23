package bot.action.groupUnit.movement;

import java.util.ArrayList;
import java.util.List;

import com.Com;

import bot.action.GenericAction;
import bot.action.groupUnit.GenericGroupAction;
import bwapi.Unit;
import newAgent.agent.OnEndActionObserver;
import newAgent.agent.group.MarineGroupAgent;

public abstract class MoveAction extends GenericGroupAction implements OnEndActionObserver {

	public MoveAction(MarineGroupAgent agent, Com com, Long maxFramesOfExecuting, boolean specialStart) {
		super(agent, com, maxFramesOfExecuting, specialStart);
		// TODO Auto-generated constructor stub
		this.executing = false;
	}

	private boolean executing;
	private int size;
	
	protected abstract bot.action.singleUnit.movement.MoveAction getMoveAction(Unit unit);
	
	private List<Unit> l;	
	private List<GenericAction> ll;
	
	@Override
	public void executeAction() {
		// TODO Auto-generated method stub
		if (!executing) {
			executing = true;
			ll = new ArrayList<>();
			l = this.groupAgent.getUnits();
			this.size = l.size();
			for (Unit unit : l) {
				bot.action.singleUnit.movement.MoveAction a = getMoveAction(unit);
				a.executeAction();
				ll.add(a);
			}
		} else {
			int i = 0, last;
			while (i < ll.size()) {
				last = ll.size();
				ll.get(i).onFrame();
				if (last == ll.size())
					i++;
			}
		}
		
		if (ll.size() == 0)
			this.onEndAction(true);
	}

	@Override
	public boolean isPossible() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onEndAction(GenericAction genericAction, boolean correct) {
		size--;
		ll.remove(genericAction);
	}

}
