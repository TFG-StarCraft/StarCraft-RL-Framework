package com;

import bot.Bot;
import qLearning.agent.Agent;
import qLearning.enviroment.SCEnviroment;

public class Com implements Runnable {

	public ComData ComData;
	public Sync Sync;

	public Com() {
		this.ComData = new ComData();
		this.Sync = new Sync();
	}

	@Override
	public void run() {
		Thread t1 = new Thread(new Bot(this));
		t1.start();

		try {
			this.Sync.s_initSync.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Starting q-Learning");

		Thread t2 = new Thread(new Agent(this, new SCEnviroment(this)));
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
		System.out.println("Restart1...");
		this.ComData.restart = true;
	}
	
}
