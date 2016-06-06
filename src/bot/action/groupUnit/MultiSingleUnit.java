package bot.action.groupUnit;

import java.util.ArrayList;
import java.util.List;

import com.Com;

import bot.action.GenericAction;
import bwapi.Unit;
import newAgent.agent.OnEndActionObserver;
import newAgent.agent.group.MarineGroupAgent;

public abstract class MultiSingleUnit extends GenericGroupAction implements OnEndActionObserver {

	public MultiSingleUnit(MarineGroupAgent agent, Com com, Long maxFramesOfExecuting, boolean specialStart) {
		super(agent, com, maxFramesOfExecuting, specialStart);
		// TODO Auto-generated constructor stub
		this.executing = false;
		this.units = this.groupAgent.getUnits();
		this.actionsFinished = new ArrayList<>();
	}

	private boolean executing;
	
	protected abstract bot.action.singleUnit.GenericUnitAction getAction(Unit unit);
	
	protected List<Unit> units;	
	protected List<GenericAction> actionsFinished;
	
	@Override
	public void executeAction() {
		// TODO Auto-generated method stub
		if (!executing) {
			executing = true;
			for (Unit unit : units) {
				bot.action.singleUnit.GenericUnitAction a = getAction(unit);
				a.executeAction();
				actionsFinished.add(a);
			}
		} else {
			int i = 0, last;
			while (i < actionsFinished.size()) {
				last = actionsFinished.size();
				actionsFinished.get(i).onFrame();
				if (last == actionsFinished.size())
					i++;
			}
		}
		
		if (actionsFinished.size() == 0)
			this.onEndAction(true);
	}

	@Override
	public void onEndAction(GenericAction genericAction, boolean correct) {
		actionsFinished.remove(genericAction);
	}

}
