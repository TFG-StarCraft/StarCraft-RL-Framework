package com;

import java.util.ArrayList;
import java.util.List;

import com.observers.AgentObserver;
import com.observers.BotOberver;
import com.observers.ComObserver;

import bot.Bot;
import qLearning.agent.Agent;
import qLearning.enviroment.SCEnviroment;

public class Com implements Runnable, AgentObserver, BotOberver {

	public ComData ComData;
	public Sync Sync;

	private ArrayList<ComObserver> observers;

	public Com(List<ComObserver> observers) {
		this();

		this.observers = new ArrayList<>(observers);
	}

	public Com() {
		this.ComData = new ComData();
		this.Sync = new Sync();
		this.observers = new ArrayList<>();
	}

	private double alpha, gamma, epsilon;

	public void configureParams(double alpha, double gamma, double epsilon) {
		this.alpha = alpha;
		this.gamma = gamma;
		this.epsilon = epsilon;
	}

	public Bot bot;
	
	@Override
	public void run() {
		bot = new Bot(this);
		Thread t1 = new Thread(bot);
		t1.start();

		try {
			this.Sync.s_initSync.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		onSendMessage("Starting q-Learning");

		Thread t2 = new Thread(new Agent(this, new SCEnviroment(this), alpha, gamma, epsilon));
		t2.start();

		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void restart() {
		onSendMessage("Restart1...");
		this.ComData.restart = true;
	}

	public void addObserver(ComObserver o) {
		this.observers.add(o);
	}

	@Override
	public void onEndIteration(int movimientos, int nume, int i) {
		for (ComObserver comObserver : observers) {
			comObserver.onEndIteration(i, movimientos, nume);
		}
	}

	@Override
	public void onEndTrain() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActionTaken() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActionFail() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSendMessage(String s) {
		for (ComObserver comObserver : observers) {
			comObserver.onSendMessage(s);
		}
	}

	public void onError(String s, boolean fatal) {
		for (ComObserver comObserver : observers) {
			comObserver.onError(s, fatal);
		}
		if (fatal)
			System.exit(-1);
	}

}
