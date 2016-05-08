package com;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.observers.AgentObserver;
import com.observers.BotOberver;
import com.observers.ComObserver;

import bot.Bot;
import newAgent.Master;
import newAgent.decisionMaker.DecisionMakerPrams;
import newAgent.event.AbstractEvent;
import utils.DebugEnum;

public class Com implements Runnable, AgentObserver, BotOberver {

	//public ComData ComData;
	//public Sync Sync;

	private ArrayList<ComObserver> observers;

	public Com(List<ComObserver> observers) {
		this();
		this.observers = new ArrayList<>(observers);
	}

	public Com() {
		this.observers = new ArrayList<>();
	}

	private double alpha, gamma, epsilon, lambda;

	public void configureParams(double alpha, double gamma, double epsilon, double lambda) {
		this.alpha = alpha;
		this.gamma = gamma;
		this.epsilon = epsilon;
		this.lambda = lambda;
	}

	private boolean startGui;
	private int startSpeed;
	
	public void configureBot(boolean gui, int speed) {
		this.startGui = gui;
		this.startSpeed = speed;
	}
	
	public Bot bot;
	public Master master;

	@Override
	public void run() {
		utils.StarcraftLauncher.launchChaosLauncher(this);

		master = new Master(this, new DecisionMakerPrams(alpha, gamma, epsilon, lambda));
		
		bot = new Bot(this, master);
		bot.frameSpeed = startSpeed;
		bot.guiEnabled = startGui;
		
		bot.run();
	}

	public void addObserver(ComObserver o) {
		this.observers.add(o);
	}

	@Override
	public void onEndIteration(int movimientos, int nume, int i, double alpha, double epsilon, Double R) {
		for (ComObserver comObserver : observers) {
			comObserver.onEndIteration(i, movimientos, nume, alpha, epsilon, R);
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

	public void onEvent(AbstractEvent abstractEvent) {
		for (ComObserver comObserver : observers) {
			comObserver.onEvent(abstractEvent);
		}
	}
	
	@Override
	public void onSendMessage(String s) {
		for (ComObserver comObserver : observers) {
			comObserver.onSendMessage(s);
		}
	}

	@Override
	public void onFpsAverageAnnouncement(double d) {
		for (ComObserver comObserver : observers) {
			comObserver.onFpsAverageAnnouncement(d);
		}
	}

	@Override
	public void onDebugMessage(String s, DebugEnum eventAtFrame) {
		for (ComObserver comObserver : observers) {
			comObserver.onDebugMessage(s, eventAtFrame);
		}
	}

	@Override
	public void onError(String s, boolean fatal) {
		for (ComObserver comObserver : observers) {
			comObserver.onError(s, fatal);
		}
		if (fatal) {
			System.err.println(s);
			System.exit(-1);
		}
	}

	public void shutSc() {
		utils.StarcraftLauncher.closeSC(this);
	}

	@Override
	public void onFullQUpdate(JPanel panel) {
		if (panel != null)
			for (ComObserver comObserver : observers) {
				comObserver.onFullQUpdate(panel);
			}
	}

}
