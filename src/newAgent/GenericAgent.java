package newAgent;

import com.Com;

import bot.Bot;
import bot.observers.GenericUnitObserver;
import bwapi.Unit;
import newAgent.decisionMaker.GenericDecisionMaker;
import newAgent.state.State;
import qLearning.environment.AbstractEnvironment;

public abstract class GenericAgent implements GenericUnitObserver, AbstractEnvironment {

	protected Com com;
	protected Bot bot;
	
	protected GenericDecisionMaker decisionMaker;
	protected Unit unit;
	
	public GenericAgent(Unit unit, Com com, Bot bot) {
		this.unit = unit;
		this.com = com;
		this.bot = bot;
	}
	
	public abstract double getReward(State state);
	
	@Override
	abstract public void onUnit(Unit unit);

	@Override
	public Unit getUnit() {
		// TODO Auto-generated method stub
		return unit;
	}
	@Override
	public void registerUnitObserver() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unRegisterUnitObserver() {
		// TODO Auto-generated method stub
		
	}

	public Com getCom() {
		return com;
	}

	public Bot getBot() {
		return bot;
	}

}
